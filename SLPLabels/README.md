# SLP labels
This is a program for creating labels for the SLP dataset.
It is setup so that the image can appear on the left hand side, with the console on the right hand side for input.
Only the keyboard is needed for input, the image will automatically refresh.
Follow the prompts for what number to input, and hit enter. There are 2 numbers for each image. First for rotation, then for arm location.
If a mistake is made, an entry of 99 will cause it to retry for that image and 101 will cause it to go back to previous image.

![Labelling setup](<Labelling setup.png>)

This project also includes an attempt to load the existing trained HRPose model which can be found in HRpose/model_dump
The config file contains further configurations for the HRPose model.
The Dana and Sim lab need to be added to the root of the directory by downloading from: https://web.northeastern.edu/ostadabbas/2019/06/27/multimodal-in-bed-pose-estimation/

run with python3 labels.py

File overview:
- Errors.py             - Defines custom exceptions
- ImageGUI.py           - GUI display for image.
- labels.py             - Main app for performing labeling
- model.py              - Attempt at loading HRPose model
- Position_labels.npy   - Binary file storing the numpy array of labels
- Position.py           - Contains definitions of the different positions matching integers to descriptions
- Positions.txt         - Description of positions
- test.py               - Just a silly test file

This project builds off the work done in the SLP dataset which can be found here: https://github.com/ostadabbas/SLP-Dataset-and-Code 

