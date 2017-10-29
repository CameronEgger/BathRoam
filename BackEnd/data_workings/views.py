# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from geopy.distance import vincenty
from django.shortcuts import render
import math, json
from data_workings.models import Bathroom
from django.http import HttpResponse
from data_workings.models import Bathroom, User
# Create your views here.

def accountService(request):
    # this is after the google signin, get the googleuserid
    req = request.POST
    googleUserId = req.get("googleUserId")
    # if the user is not in the database
    if not User.objects.filter(googleUserId=googleUserId).exists():
        User.objects.create(
            googleUserId=googleUserId,
            handle="default",
            points=0
        )
        return HttpResponse("new account!")
    else:
        user = User.objects.get(googleUserId=googleUserId)
        user_data = {
            "googleUserId":user.googleUserId,
            "handle":user.handle,
            "points":user.points
        }
        return HttpResponse(json.dumps(user_data), content_type="application/json")

    # handle = req.get("handle")

def updateHandle(request):
    req = request.POST
    googleUserId = req.get("googleUserId")
    new_handle = req.get("new_handle")
    user_object = User.objects.get(googleUserId=googleUserId)
    user_object.handle = new_handle
    user_object.save()
    return HttpResponse("success!")

def tryToUpvote(request):
    #grabs the users Id, latitude, longitude, and height. grabs the bathrooms ID.
    req = request.POST
    googleUserId = req.get("googleUserId")
    latitude = req.get("latitude")
    longitude = req.get("longitude")
    height = req.get("height")
    bathroomId = req.get("bathroomId")

    # if either the specified bathroom or user does not exist, we want this to fail
    user = User.objects.get(googleUserId=googleUserId)
    if not user:
        raise Exception("the user that tried to upvote does not exist!")

    bathroom = Bathroom.objects.get(id=bathroomId)
    if not bathroom:
        raise Exception("the bathroom that the user tried to upvote does not exist!")

    #if the user is within 10 meters.
    if is_in_range((latitude,longitude),(bathroom.latitude,bathroom.longitude),10):
        #increment points of both and save
        user.points += 1
        user.save()

        bathroom.points += 1
        bathroom.save()
        return HttpResponse("success!")

    return HttpResponse("fail!")

def retrCoord(request):
    # this request should contain the lat long & height of the user
    # it will return either "no" or the json representation of the closest bathroom
    req = request.POST
    latitude = float(req.get("latitude"))
    longitude = float(req.get("longitude"))
    height = float(req.get("height"))

    bathroom = getNearestBathroom(longitude, latitude, height);
    # bathroom will end up being a json thing

    # this will be none if there is not a close bathroom
    if bathroom == None:
        return HttpResponse("no")
    return HttpResponse(bathroom, content_type="application/json")

def createCoord(request):
    # print "we called this"
    # for this we want to assert that the creation should have
    # lat long height coords, a name, a description, a public value, a gender,
    # created by
    req = request.POST
    latitude = float(req.get("latitude"))
    longitude = float(req.get("longitude"))
    height = float(req.get("height"))
    title = req.get("title")
    comment = req.get("comment")
    public = bool(req.get("public"))
    gender = req.get("gender")
    created_by = req.get("created_by")

    Bathroom.objects.create(
        latitude = latitude,
        longitude = longitude,
        height = height,
        title = title,
        comment = comment,
        publicly_accessible = public,
        created_by = created_by,
        points = 1
    )

    return HttpResponse("hey")

def bathroomsInRadiusView(request):
    req = request.POST
    latitude = float(req.get("latitude"))
    longitude = float(req.get("longitude"))
    radius = float(req.get("radius"))

    listOfBathrooms = getBathroomsRadius(longitude,latitude,radius)

    return HttpResponse(listOfBathrooms, content_type="application/json")

def getNearestBathroom(x, y, z):
    bathroomsQueryList = Bathroom.objects.all()
    shortestCandidate = None
    shortest = 2147483647
    for bath in bathroomsQueryList:
        xp = (x - bath.longitude) * (x - bath.longitude)
        yp = (y - bath.latitude) * (y - bath.latitude)
        zp = (z - bath.height) * (z - bath.height)
        distance = math.sqrt(xp + yp + zp)
        if distance < shortest:
            shortest = distance
            shortestCandidate = bath
    if shortest != 2147483647:
        return shortestCandidate.return_bathroom_data_json_with_distance_to(shortest)
    return None

# distances are calculated in meters
def getBathroomsRadius(longitude, latitude, radius):
    bathroomsQueryList = Bathroom.objects.all()
    inRadius = []
    my_loc = (longitude, latitude)
    for bath in bathroomsQueryList:
        bath_loc = (bath.longitude, bath.latitude)
        distance = vincenty(my_loc, bath_loc).meters
        if distance <= radius:
            inRadius.append(bath.return_bathroom_data_json_with_distance_to(distance))

    # this is a list containing all the instances of Bathroom that are within the radius of the player
    return json.dumps(inRadius)

def is_in_range(player_cords, bathroom_cords, radius):
    # these are both tuples (latitude, longitude)
    meters_distance = vincenty(player_cords,bathroom_cords).meters
    print meters_distance
    if meters_distance <= radius:
        return True
    return False
