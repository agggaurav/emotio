from django.shortcuts import render
from django.shortcuts import get_object_or_404
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from .models import User
from .models import Post
from .models import Like
from .models import Follower
from .serializers import userSerializer
from .serializers import postSerializer
from .serializers import followerSerializer
from .serializers import likeSerializer
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
import timeit
import operator

#all the apis having some need for POST will be updated...this is for testing


@api_view(['GET', 'POST'])
def getUserList(request):
    """
    List all snippets, or create a new snippet.
    """
    if request.method == 'GET':
        users = User.objects.all()
        #users = User.objects.raw("delete from socialnetwork_user where name='ram'")
        serializer = userSerializer(users, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = userSerializer(data=request.data)
        #print (serializer.data) 
        if serializer.is_valid(raise_exception=True):
            print "image"
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


#getProfile on the basis of email_id
@api_view(['GET', 'PUT', 'DELETE'])
def getProfile(request, email_id):
    """
    Retrieve, update or delete a snippet instance.
    """
    try:
        users = User.objects.get(email_id=email_id)        
    except users.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = userSerializer(users) 
        print (serializer.data)
        return Response(serializer.data)

    elif request.method == 'PUT':
        serializer = userSerializer(users, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        users.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)



@api_view(['GET','POST'])
def postProfilepic(request, email_id):
    """
    Retrieve, update or delete a snippet instance.
    """
    try:
        users = User.objects.get(email_id=email_id)        
    except users.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = userSerializer(users) 
        print (serializer.data)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = userSerializer(users, data=request.data,partial=True)
        if serializer.is_valid(raise_exception=True):
            #print serializer.data		
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

   

		
		
		
#post

@api_view(['GET', 'POST'])
def getPosts(request):
    """
    List all snippets, or create a new snippet.
    """
    if request.method == 'GET':
        posts = Post.objects.all().order_by('-posted_on')
        serializer = postSerializer(posts,many=True)
        #a=serializer.data[1]
        #print a
        #print a.items()[4][1]	
        #usersname=User.objects.get(pk=a.items()[4][1])
        #print usersname.name
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer =postSerializer(data=request.data)
        print "image"
        if serializer.is_valid(raise_exception=True):
            print "image"
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)



@api_view(['GET', 'PUT', 'DELETE'])
def updateLikes(request, pk):
    """
    Retrieve, update or delete a snippet instance.
    """
    try:
        posts = Post.objects.get(pk=pk)
        posts.no_of_likes=posts.no_of_likes+1
        posts.save()
    except posts.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = postSerializer(posts) 
        print (serializer.data)
        return Response({serializer.data['posted_on'],serializer.data['no_of_likes']})

    elif request.method == 'PUT':
        serializer = postSerializer(posts, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        posts.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)



##likes
@api_view(['GET', 'POST'])
def like_list(request):
    """
    List all snippets, or create a new snippet.
    """
    if request.method == 'GET':
        likes = Like.objects.all()
        serializer =likeSerializer(likes, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer =likeSerializer(data=request.data)
        print "image"
        if serializer.is_valid(raise_exception=True):
            print "image"
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)




@api_view(['GET', 'PUT', 'DELETE'])
def like_detail(request, pk):
    """
    Retrieve, update or delete a snippet instance.
    """
    try:
        likes = Like.objects.get(post_id=pk)
    except likes.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = likeSerializer(likes,many=True) 
        print (serializer.data)
        return Response({serializer.data['post_id'],serializer.data['liked_by']})

    elif request.method == 'PUT':
        serializer = likeSerializer(likes, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        likes.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

##raw query bhi krskte hai but usme query performed but uske baad serialiser se print krne mei dikkat aarhiha
