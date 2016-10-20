from rest_framework import serializers

from .models import User
from .models import Post
from .models import Like
from .models import Follower

import base64
from django.core.files.base import ContentFile

class userSerializer(serializers.ModelSerializer):

	#purl_status = serializers.SerializerMethodField('get_image_url')
	
	class Meta:
		model=User
		#fields=('ticker','open','volume')
		fields= '__all__'	
		
	#def get_image_url(self, obj):
	#	return obj.profilepic.url


class postSerializer(serializers.ModelSerializer):
    class Meta:
        model=Post
        fields='__all__'
        



class followerSerializer(serializers.ModelSerializer):
    class Meta:
        model=Follower
        fields='__all__'

    

class likeSerializer(serializers.ModelSerializer):
    class Meta:
        model=Like
        fields='__all__'

    
