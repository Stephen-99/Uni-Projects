import numpy as np

with open('Position_labels.npy', 'rb') as np_file:
    a = np.load(np_file)
print(a[0])
