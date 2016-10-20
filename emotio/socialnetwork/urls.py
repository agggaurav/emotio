from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
from socialnetwork import views

urlpatterns = [
    url(r'^user/$', views.getUserList),
    url(r'^user/(?P<email_id>[\w.%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4})/$', views.getProfile),
    url(r'^user/profilepic/(?P<email_id>[\w.%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4})/$', views.postProfilepic),
    url(r'^post/$', views.getPosts),
    url(r'^post/(?P<pk>[0-9]+)$', views.updateLikes),


   # url(r'^follower/$', views.follower_list),
    #url(r'^follower/(?P<pk>[0-9]+)$', views.follower_detail),


    url(r'^like/$', views.like_list),
    url(r'^like/(?P<pk>[0-9]+)$', views.like_detail),

]

urlpatterns = format_suffix_patterns(urlpatterns)
