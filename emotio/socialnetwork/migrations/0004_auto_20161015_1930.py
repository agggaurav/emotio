# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-10-15 19:30
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('socialnetwork', '0003_auto_20161015_1556'),
    ]

    operations = [
        migrations.RenameField(
            model_name='like',
            old_name='post',
            new_name='post_id',
        ),
        migrations.AlterField(
            model_name='like',
            name='liked_by',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='socialnetwork.User'),
        ),
    ]