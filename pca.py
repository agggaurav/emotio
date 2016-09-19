from PIL import Image
from numpy import *
import numpy as np
import pylab
import cv2

def pca(X):
  # Principal Component Analysis
  # input: X, matrix with training data as flattened arrays in rows
  # return: projection matrix (with important dimensions first),
  # variance and mean

  #get dimensions
  dim=128
  num_data= X.shape
  print X.shape
  #center data
  mean_X = X.mean(axis=0)
  for i in range(int(num_data[0])):
      X[i] -= mean_X

  if dim>100:
      print 'PCA - compact trick used'
      M = dot(X,X.T) #covariance matrix
      e,EV = linalg.eigh(M) #eigenvalues and eigenvectors
      tmp = dot(X.T,EV).T #this is the compact trick
      V = tmp[::-1] #reverse since last eigenvectors are the ones we want
      S = sqrt(e)[::-1] #reverse since eigenvalues are in increasing order
  else:
      print 'PCA - SVD used'
      U,S,V = linalg.svd(X)
      V = V[:num_data] #only makes sense to return the first num_data

  #return the projection matrix, the variance and the mean
  return V,S,mean_X

# Load an color image in grayscale
img = cv2.imread('/home/gaurav/Desktop/images.jpeg',0)
img2=cv2.imread('/home/gaurav/f1.jpg',0)
orb = cv2.ORB_create()
sift = cv2.xfeatures2d.SIFT_create()
(kps, descs) = sift.detectAndCompute(img2, None)
kp = sift.detect(img2,None)
img2=cv2.drawKeypoints(img2,kp,None)
cv2.imwrite('sift_keypoints3.jpg',img2)
print("# kps: {}, descriptors: {}".format(len(kps), descs.shape))
x=np.array(descs)
print x
np.savetxt('text.txt',x,fmt='%.2f')
#print x.shape[0:2]
#print np.shape(x)
#print descs[6]
#print descs[6][2]
target = open('descs.txt', 'w')
target.write(descs[0])
target.write("\n")
target.write(descs[1])
target.close()

im = np.array(descs) #open one image to get the size
m,n = im.shape[0:2] #get the size of the images
#imnbr = len(descs) #get the number of images

#create matrix to store all flattened images
#immatrix = numpy.array([numpy.array(Image.open(imlist[i])).flatten() for i #in range(imnbr)],'f')
immatrix=np.array(descs).flatten()
kmp=immatrix.shape
#perform PCA
V,S,immean = pca(immatrix)

#mean image and first mode of variation
immean = immean.reshape(m,n)
mode = V[0].reshape(m,n)

#show the images
pylab.figure()
pylab.gray()
pylab.imshow(immean)

pylab.figure()
pylab.gray()
pylab.imshow(mode)

pylab.show()
