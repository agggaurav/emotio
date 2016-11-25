
from __future__ import unicode_literals


from django.db import models

#django maintains coloumn ID which has auto increment
# Create your models here.
###**********apply foreign key====and may to many relationship==

class User(models.Model):
    #posts = models.ManyToManyField(Post)
    #user_id=models.IntegerField(blank=True, null=True)
    name=models.CharField(max_length=100,blank=True, null=True)
    email_id=models.CharField(max_length=200,blank=True, null=True)
    password = models.CharField(max_length=100,blank=True, null=True)
    phone_no = models.CharField(max_length=50,blank=True, null=True)
    gender=models.CharField(max_length=30,blank=True, null=True)
    description=models.TextField(blank=True,null=True)
    # store as a string (URL) or direct image as in image field
    #profile_image_url=models.CharField(max_length=1000,blank=True, null=True)
    profile_pic = models.ImageField(upload_to='uploaded_pic/', null=True, blank=True)
    liked_pics=[0]

class Post(models.Model):
    user = models.ForeignKey(User,on_delete=models.CASCADE,blank=True,null=True)
    username=models.CharField(max_length=100,blank=True, null=True)
    email_id=models.CharField(max_length=200,blank=True, null=True)
    #likes = models.ManyToManyField(Like)
    #Post_id=models.IntegerField(blank=True, null=True)
    image_posted = models.ImageField(upload_to='uploaded_pic/',blank=True,null=True)
    #posted_by =models.IntegerField(blank=True, null=True)
    no_of_likes = models.IntegerField(default=0,blank=True, null=True)
    posted_on= models.DateTimeField(auto_now_add=True)
    #add_now for updated on
    #auto_now_add created on

class Follower(models.Model):
    fuser_id = models.IntegerField(blank=True, null=True)
    followed_by = models.IntegerField(blank=True, null=True)

class Like(models.Model):
    post_id = models.IntegerField(blank=True, null=True)
    liked_by = models.CharField(max_length=200,blank=True, null=True)
    
    
#liked_by=models.IntegerField(blank=True, null=True)



