# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-10-15 13:28
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('socialnetwork', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='post',
            name='Post_id',
        ),
        migrations.RemoveField(
            model_name='user',
            name='user_id',
        ),
    ]
