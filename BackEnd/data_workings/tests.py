# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.test import TestCase, Client
from data_workings.models import Bathroom, User, Upvote
import json
# def testSearch():
# 	#tests that an empty database would have nothing nearby.
# 	if retrCoord(5, 5) != False:
#         raise Exception("Test case failed line 9")
#     #John, add stuff to the data base after the first test
#     #add one at 1, 2 and one at 5,5
#
#
#     #tests that a non-empty database would have something nearby.
#     if !retrCoord(5,5) == False:
#         raise Exception("Test case failed line 16")

class BathroomCreationTest(TestCase):
	def setUp(self):
		self.client = Client()

	def test_creation(self):
		test_data = {
			"latitude":10,
			"longitude":10,
			"height":10,
			"title":"test1",
			"comment":"this should work",
			"public":"True",
			"gender":"m",
			"created_by":1
		}
		response = self.client.post("/create/", test_data)
		testQuerySet = Bathroom.objects.filter(title="test1")
		self.assertEqual(testQuerySet.count(), 1)
		testBathroom = testQuerySet.first()
		self.assertEqual(testBathroom.title,"test1")

class NearestBathroomTest(TestCase):
	def setUp(self):
		self.client = Client()

	def test_find(self):
		Bathroom.objects.create(
			latitude = 9,
	        longitude = 9,
	        height = 9,
	        title = "test2",
	        comment = "this is a test bathroom",
	        publicly_accessible = "True",
	        created_by = 1,
	      	points = 1,
		)

		userData = {
			"latitude":8,
			"longitude":8,
			"height":8
		}

		response = self.client.post('/coordinates/', userData)
		json_representation_of_closest_bathroom = json.loads(response.content)

		self.assertEqual(json_representation_of_closest_bathroom["title"],"test2")

class BathroomsInRadiusTest(TestCase):
	def setUp(self):
		self.client = Client()

	def test_bathrooms_in_radius(self):
		Bathroom.objects.create(
			latitude = 6,
	        longitude = 6,
	        height = 6,
	        title = "test1",
	        comment = "this is a test bathroom. it shouldn't pass.",
	        publicly_accessible = "True",
	        created_by = 1,
	      	points = 1,
		)

		Bathroom.objects.create(
			latitude = 7,
	        longitude = 7,
	        height = 7,
	        title = "test2",
	        comment = "this is a test bathroom",
	        publicly_accessible = "True",
	        created_by = 1,
	      	points = 1,
		)

		Bathroom.objects.create(
			latitude = 9,
	        longitude = 9,
	        height = 9,
	        title = "test3",
	        comment = "this is a test bathroom",
	        publicly_accessible = "True",
	        created_by = 1,
	      	points = 1,
		)

		userData = {
			"latitude":8,
			"longitude":8,
			"radius":200000
		}

		response = self.client.post('/radius/', userData)
		print response
		list_of_closest_bathrooms = json.loads(response.content)

		self.assertEqual(len(list_of_closest_bathrooms),2)

class UpvotingBathroomsTest(TestCase):
	def setUp(self):
		self.client = Client()

	def test_bathroom_upvoting(self):
		Bathroom.objects.create(
			latitude = 9,
	        longitude = 9,
	        height = 9,
	        title = "test3",
	        comment = "this is a test bathroom",
	        publicly_accessible = "True",
	        created_by = 1,
	      	points = 1,
		)

		User.objects.create(
			googleUserId="test",
			handle="testUser",
			points=0
		)

		userData = {
			"googleUserId":"test",
			"latitude":9.00001,
			"longitude":9.00001,
			"height":9.0001,
			"bathroomId":1
		}

		response = self.client.post("/upvote/",userData)
		self.assertEqual(User.objects.get(googleUserId="test").points,1)
		self.assertEqual(Bathroom.objects.get(id=1).points,2)

class GeneralAccountTest(TestCase):
	def setUp(self):
		self.client = Client()

	def test_new_account_creation(self):
		clientData = {
			"googleUserId":"testtest"
		}
		response = self.client.post("/signIn/",clientData)
		# make sure we get the indication that a new account was created
		self.assertEqual(response.content, "new account!")
		# make sure the new account exists in the database
		self.assertEqual(User.objects.filter(googleUserId="testtest").exists(),True)

	def test_existing_account_signin(self):
		clientData = {
			"googleUserId":"testtest"
		}
		response = self.client.post("/signIn/",clientData)
		# make sure we get the indication that a new account was created
		self.assertEqual(response.content, "new account!")

		response = self.client.post("/signIn/",clientData)
		self.assertEqual(json.loads(response.content)["points"],0)

	def test_handle_change(self):
		clientData = {
			"googleUserId":"testtest"
		}
		response = self.client.post("/signIn/",clientData)
		# make sure we get the indication that a new account was created
		self.assertEqual(response.content, "new account!")

		secondClientData = {
			"googleUserId":"testtest",
			"new_handle":"fucc"
		}

		response = self.client.post("/updateHandle/",secondClientData)
		self.assertEqual(response.content,"success!")
		response_dict = json.loads(self.client.post("/signIn/",clientData).content)
		self.assertEqual(response_dict["handle"],"fucc")
