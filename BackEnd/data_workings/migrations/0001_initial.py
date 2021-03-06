# -*- coding: utf-8 -*-
# Generated by Django 1.11.6 on 2017-10-28 20:21
from __future__ import unicode_literals

import datetime
from django.db import migrations, models
from django.utils.timezone import utc


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Bathroom',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=40)),
                ('latitude', models.FloatField()),
                ('longitude', models.FloatField()),
                ('height', models.FloatField()),
                ('gender', models.CharField(choices=[('m', 'male'), ('f', 'female'), ('n', 'neutral')], max_length=1)),
                ('comment', models.CharField(max_length=160)),
                ('publicly_accessible', models.BooleanField()),
                ('created_by', models.CharField(max_length=60)),
                ('created_at', models.DateTimeField(default=datetime.datetime(2017, 10, 28, 20, 21, 19, 373315, tzinfo=utc))),
                ('points', models.IntegerField()),
            ],
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('googleUserId', models.CharField(max_length=60)),
            ],
        ),
    ]
