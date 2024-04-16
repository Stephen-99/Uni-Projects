
    #Load the HRPose model from HRpose/
        #I will probably need the class files for HRpose.
    #run all the training data through HRpose to get the joints data
        #Or run it as we run the model 
            #This will be slower, but the final model will need to pass through all the classifiers.
            #Don't do this for training!
    #Create a basic NN classifer and train it on the joint data from the HRpose model and the labels I created
    #Use the output labels from this classifer + the joint data and labels to create the hand classifing model
    #put the 3 models together as a stacked algorithm and make sure it works for inference through all 3.

from multiprocessing import freeze_support
import torch
from torch.utils.data import DataLoader

from HRpose.HRpose import PoseHighResolutionNet
from HRpose.config_HRP.default import _C as cfg
from HRpose.config_HRP.default import update_config
from HRpose.config_HRP.models import MODEL_EXTRAS
from HRpose.data.SLP_FD import SLP_FD
from HRpose.data.SLP_RD import SLP_RD
from HRpose.opt import parseArgs

def main():
    PATH = "HRpose/model_dump/model_best.pth"

    cfg['MODEL']['EXTRA'] = MODEL_EXTRAS['pose_high_resolution_net']
    model = PoseHighResolutionNet(cfg, 1)
    model.init_weights(PATH) 

    #SIGH, this is still not right. It has unexpected keys...
    #model.load_state_dict(torch.load(PATH)['state_dict'])

    #model._load_from_state_dict(self, state_dict, prefix, local_metadata, strict,
    #                             missing_keys, unexpected_keys, error_msgs)


    #model = torch.nn.DataParallel(model, device_ids=opts.gpu_ids).cuda()
    #cuda stuff??
    model.eval()

    opts = parseArgs() #create all the default arguments that were used wheen running HRPose
    opts.SLP_fd = "danaLab"

    SLP_rd_test = SLP_RD(opts, phase=opts.test_par)  # all test result      # can test against all controled in opt
    SLP_fd_test = SLP_FD(SLP_rd_test, opts, phase='test', if_sq_bb=True)

    dataloader = DataLoader(dataset=SLP_fd_test, batch_size = 32,
                                shuffle=False, num_workers=opts.n_thread, pin_memory=opts.if_pinMem)

    # Make predictions on all of the data at once


    #REDO THIS based on the validate function in main.py (line 140)
    predictions = None
    with torch.no_grad():

        for data in dataloader:
            print(data)
            predictions = model(data)

    print(predictions)


if __name__ == "__main__":
    freeze_support()
    main()