import cv2
import numpy as np
import os
from sklearn import svm
from sklearn import datasets
from sklearn.svm import SVC

image_paths = []
path = "/home/gaurav/ck/cohn-kanade/S010"

#list of our class names
training_names = os.listdir(path)
print training_names
training_paths = []
names_path = []
#get full list of all training images
for p in training_names:
    training_paths1 = os.listdir("/home/gaurav/ck/cohn-kanade/S010/"+p)
    for j in training_paths1:
        training_paths.append("/home/gaurav/ck/cohn-kanade/S010/"+p+"/"+j)
        names_path.append(p)
orb = cv2.ORB_create()
sift = cv2.xfeatures2d.SIFT_create()
print names_path

descriptors_unclustered = []

dictionarySize = 6

BOW = cv2.BOWKMeansTrainer(dictionarySize)

for p in training_paths:
    print p
    gray = cv2.imread(p,0)
    #gray = cv2.cvtColor(image, cv2.CV_LOAD_IMAGE_GRAYSCALE)
    kp, dsc= sift.detectAndCompute(gray, None)
    BOW.add(dsc)

#dictionary created
dictionary = BOW.cluster()


FLANN_INDEX_KDTREE = 0
index_params = dict(algorithm = FLANN_INDEX_KDTREE, trees = 5)
search_params = dict(checks=50)   # or pass empty dictionary
flann = cv2.FlannBasedMatcher(index_params,search_params)
sift2 = cv2.xfeatures2d.SIFT_create()
bowDiction = cv2.BOWImgDescriptorExtractor(sift2, cv2.BFMatcher(cv2.NORM_L2))
bowDiction.setVocabulary(dictionary)
print "bow dictionary", np.shape(dictionary)
print dictionary

#returns descriptor of image at pth
def feature_extract(pth):
    gray = cv2.imread(pth,0)
    #gray = cv2.cvtColor(im, cv2.CV_LOAD_IMAGE_GRAYSCALE)
    return bowDiction.compute(gray, sift.detect(gray))

train_desc = []
train_labels = []
i = 0
for p in training_paths:
    train_desc.extend(feature_extract(p))
    print names_path[i]
    if names_path[i]=='001':
        train_labels.append(1)
    if names_path[i]=='002':
        train_labels.append(2)
    if names_path[i]=='003':
        train_labels.append(3)
    if names_path[i]=='004':
        train_labels.append(4)
    if names_path[i]=='005': 
        train_labels.append(5)
    if names_path[i]=='006':
	train_labels.append(6)
    i = i+1

print "svm items", len(train_desc), len(train_desc[0])
count=0
#svm = cv2.SVM()
#svm.train(np.array(train_desc), np.array(train_labels))
clf = svm.SVC()
clf.fit(np.array(train_desc),np.array(train_labels))  
SVC(C=1.0, cache_size=200, class_weight=None, coef0=0.0,  decision_function_shape=None, degree=3, gamma='auto', kernel='rbf',  max_iter=-1, probability=False,random_state=None, shrinking=True,  tol=0.001, verbose=False)

i=0
j=0
"""
confusion = np.zeros((5,5))
def classify(pth):
    feature = feature_extract(pth)
    p = svm.predict(feature)
    confusion[train_labels[count]-1,p-1] = confusion[train_labels[count]-1,p-1] +1
    
    

for p in training_paths:
    classify(p)
    count+=1

def normalizeRows(M):
    row_sums = M.sum(axis=1)
    return M / row_sums
    
confusion = normalizeRows(confusion)

confusion = confusion.transpose()
    
print confusion"""
