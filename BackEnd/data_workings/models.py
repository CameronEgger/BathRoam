# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.db import models
from django.utils import timezone
import json


# all of these have an id, which is their unique primary key
class Bathroom(models.Model):
    # male/female, coords, comment field, title,
    # optional picture, some indicator about whether or not it is public
    title = models.CharField(max_length = 40)
    latitude = models.FloatField()
    longitude = models.FloatField()
    height = models.FloatField()
    gender = models.CharField(choices=[('m','male'),('f','female'),('n','neutral')], max_length=1) # male female neutral
    comment = models.CharField(max_length = 160)
    publicly_accessible = models.BooleanField()
    created_by = models.CharField(max_length = 60) # this should be the googleUserId of a User
    created_at = models.DateTimeField(default=timezone.now)
    points = models.IntegerField()

    def __str__(self):
        return self.title

    def return_bathroom_data_json_with_distance_to(self,distance_to):
        data = {
            "distance_to":distance_to,
            "gender":self.gender,
            "latitude":self.latitude,
            "longitude":self.longitude,
            "height":self.height,
            "comments":self.comment,
            "title":self.title,
            "public":self.publicly_accessible,
            "creator":self.created_by,
            "points":self.points,
            "creation_date":self.created_at.isoformat(),
            "id":self.id
        }
        return json.dumps(data)

    def return_bathroom_data_json(self):
        data = {
            "gender":self.gender,
            "latitude":self.latitude,
            "longitude":self.longitude,
            "height":self.height,
            "comments":self.comment,
            "title":self.title,
            "public":self.publicly_accessible,
            "creator":self.created_by,
            "points":self.points,
            "creation_date":self.created_at.isoformat(),
            "id":self.id
        }
        return json.dumps(data)

    # TODO- finish this
    def can_upvote(self, user): # user will be the id of the user
        queryS = Upvote.all()
        if queryS.contains(user):
            return False
        return True



class User(models.Model):
    googleUserId = models.CharField(max_length = 60) # this is whatever the persistent identifyer from google will be
    handle = models.CharField(max_length = 30)
    points = models.IntegerField()


class Upvote(models.Model):
    user = models.IntegerField() # this is the id of the user that did the upvote
    date = models.DateTimeField(default=timezone.now)
    bathroom = models.CharField(max_length = 60) # this is the id of the bathroom that the upvote is for
