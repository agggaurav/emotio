from django.contrib import admin
from .models import User
from .models import Post
from .models import Like
from .models import Follower

# Register your models here.
admin.site.register(User)
admin.site.register(Post)
admin.site.register(Like)
admin.site.register(Follower)


