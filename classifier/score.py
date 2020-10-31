import numpy as np
import matplotlib.pyplot as plt
from itertools import cycle

from sklearn import svm, datasets
from sklearn.metrics import roc_curve, auc, roc_auc_score, f1_score
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import label_binarize, LabelBinarizer
from sklearn.multiclass import OneVsRestClassifier


def f1(y_true, y_pred):
    return f1_score(y_true, y_pred, average='weighted')


def multiclass_roc_auc_score(y_test, y_pred, average="macro"):
    lb = LabelBinarizer()
    lb.fit(y_test)
    y_test = lb.transform(y_test)
    y_pred = lb.transform(y_pred)
    return roc_auc_score(y_test, y_pred, average=average)


def roc(n_classes, y_test, y_score):
    macro_roc_auc_ovo = roc_auc_score(y_test, y_score)
    print(macro_roc_auc_ovo)
    # weighted_roc_auc_ovo = roc_auc_score(y_test, y_score, multi_class="ovo",
    #                                      average="weighted")
    # macro_roc_auc_ovr = roc_auc_score(y_test, y_score, multi_class="ovr",
    #                                   average="macro")
    # weighted_roc_auc_ovr = roc_auc_score(y_test, y_score, multi_class="ovr",
    #                                      average="weighted")
    # print("One-vs-One ROC AUC scores:\n{:.6f} (macro),\n{:.6f} "
    #       "(weighted by prevalence)"
    #       .format(macro_roc_auc_ovo, weighted_roc_auc_ovo))
    # print("One-vs-Rest ROC AUC scores:\n{:.6f} (macro),\n{:.6f} "
    #       "(weighted by prevalence)"
    #       .format(macro_roc_auc_ovr, weighted_roc_auc_ovr))
    # fpr = dict()
    # tpr = dict()
    # roc_auc = dict()
    # all_fpr = np.unique(np.concatenate([fpr[i] for i in range(n_classes)]))
    #
    # # Then interpolate all ROC curves at this points
    # mean_tpr = np.zeros_like(all_fpr)
    # for i in range(n_classes):
    #     mean_tpr += np.interp(all_fpr, fpr[i], tpr[i])
    #
    # # Finally average it and compute AUC
    # mean_tpr /= n_classes
    #
    # fpr["macro"] = all_fpr
    # tpr["macro"] = mean_tpr
    # roc_auc["macro"] = auc(fpr["macro"], tpr["macro"])
    #
    # # Plot all ROC curves
    # lw=3
    # plt.figure()
    # plt.plot(fpr["micro"], tpr["micro"],
    #          label='micro-average ROC curve (area = {0:0.2f})'
    #                ''.format(roc_auc["micro"]),
    #          color='deeppink', linestyle=':', linewidth=4)
    #
    # plt.plot(fpr["macro"], tpr["macro"],
    #          label='macro-average ROC curve (area = {0:0.2f})'
    #                ''.format(roc_auc["macro"]),
    #          color='navy', linestyle=':', linewidth=4)
    #
    # colors = cycle(['aqua', 'darkorange', 'cornflowerblue'])
    # for i, color in zip(range(n_classes), colors):
    #     plt.plot(fpr[i], tpr[i], color=color, lw=lw,
    #              label='ROC curve of class {0} (area = {1:0.2f})'
    #                    ''.format(i, roc_auc[i]))
    #
    # plt.plot([0, 1], [0, 1], 'k--', lw=lw)
    # plt.xlim([0.0, 1.0])
    # plt.ylim([0.0, 1.05])
    # plt.xlabel('False Positive Rate')
    # plt.ylabel('True Positive Rate')
    # plt.title('Some extension of Receiver operating characteristic to multi-class')
    # plt.legend(loc="lower right")
    # plt.show()