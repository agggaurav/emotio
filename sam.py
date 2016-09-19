import numpy as np
import cv2

# Load an color image in grayscale
img = cv2.imread('/home/gaurav/Desktop/S080_001_00214916.png',0)
img2=cv2.imread('/home/gaurav/Desktop/S080_001_00214916.png',0)
orb = cv2.ORB_create()
sift = cv2.xfeatures2d.SIFT_create()
(kps, descs) = sift.detectAndCompute(img2, None)
kp = sift.detect(img2,None)
img2=cv2.drawKeypoints(img2,kp,None)
cv2.imwrite('sift_keypoints3.jpg',img2)
print("# kps: {}, descriptors: {}".format(len(kps), descs.shape))
x=np.array(descs)
#print np.shape(x)
print descs[6]
print descs[6][2]
target = open('descs.txt', 'w')
target.write(descs[0])
target.write("\n")
target.write(descs[1])
target.close()
