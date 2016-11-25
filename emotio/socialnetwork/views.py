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
import json

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

   

@api_view(['GET', 'POST'])
def postuserdetail(request):
    """
    List all snippets, or create a new snippet.
    """
   
		
    if request.method == 'GET':
        users = User.objects.all()
        #users = User.objects.raw("delete from socialnetwork_user where name='ram'")
        serializer = userSerializer(users, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        user = User.objects.get(email_id=request.data['email_id'])
        serializer = postSerializer(data=request.data)
        print serializer.initial_data 
        if serializer.is_valid(raise_exception=True):
            print "data  ",serializer
            serializer.save()
            postw=Post.objects.get(pk=serializer.data['id'])
            postw.user=user
            postw.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)		

def op(id):
    post = Post.objects.get(id=id)	
    post.username="qwe"
    serializer=postSerializer(post,many=True)
    print "bybyby   ",list(serializer)
		
#post

@api_view(['GET', 'POST'])
def getPosts(request):
    """
    List all snippets, or create a new snippet.
    """
    if request.method == 'GET':
        posts = Post.objects.all().order_by('-posted_on')
        #likes=Like.objects.all().get(liked_by=posts[0].email_id)
        #print posts[0].posted_on
        for a in posts:
            uperson= a.user
            if (uperson is not None):
               # print uperson.name
                a.username=uperson.name
        serializer = postSerializer(posts,many=True)
        #lkserial= likeSerializer(likes,many=True)
        a=serializer.data
        #print a
        #print a.items()[6][1]	
        #usersname=User.objects.get(pk=a.items()[6][1])
        #print usersname.name
        #print posts[0].image_posted
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
    """
	try:
        posts = Post.objects.get(pk=pk)
        if(posts.no_of_likes is not None):
            posts.no_of_likes=posts.no_of_likes+1
        else:
            posts.no_of_likes=0
        posts.save()
    except posts.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
	"""
	
    if request.method == 'GET':
        posts=Post.objects.get(pk=pk)
        serializer = postSerializer(posts) 
        #serializer.data['no_of_likes']=serializer.data['no_of_likes']+1
        print (serializer.data['username'])
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
        posts = Post.objects.get(pk=request.data['post_id'])
        user=User.objects.get(email_id=request.data['liked_by'])
        if(posts.no_of_likes is not None):
            posts.no_of_likes=posts.no_of_likes+1
        else:
            posts.no_of_likes=1
        posts.save()
        serializer =likeSerializer(data=request.data)
        print request.data['post_id']
        if serializer.is_valid(raise_exception=True):
            print "image"
            serializer.save()
            (user.liked_pics).append(request.data['post_id'])
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)




@api_view(['GET', 'PUT', 'DELETE'])
def like_detail(request, email_id):
    """
    Retrieve, update or delete a snippet instance.
    """
    """
    try:
        likes = Like.objects.get(liked_by="pankaj@gmail.com")
    except likes.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    """

    if request.method == 'GET':
        likes = Like.objects.filter(liked_by=email_id)
        serializer = likeSerializer(likes,many=True) 
        #user=User.objects.get(pk="gaurav23dec@gmail.com")
        #print user.liked_pics
        print (serializer.data)
        #return Response({serializer.data['post_id'],serializer.data['liked_by']})
        return Response(serializer.data)

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
