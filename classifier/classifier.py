import pymongo
import shutil

from utils import *
from score import *


def predict(docs):
    y_true = []
    y_pred = []
    for i in docs:
        y_true.append(get_doc_label(i["fileName"]))
        y_pred.append(get_type(i["text"])[1])

    return y_true, y_pred


def classify(doc):
    doc_type = get_type(doc['text'])
    coll.update_one({"_id": doc['_id']}, {'$set': {"type": doc_type[0], "type_id": doc_type[1]}})
    doc["type"] = doc_type[0]
    doc["type_id"] = doc_type[1]


def calc_score(docs):
    y_true, y_pred = predict(docs)

    return {
        'f1': f1(y_true, y_pred),
        'roc': multiclass_roc_auc_score(y_true, y_pred)
    }


def parse_type_from_file(path):
    with open(path, 'r', encoding='utf8', errors='replace') as f:
        return get_type(f.read())


def find_bad_classification(docs):
    y_t, y_p = predict(docs)
    for i in range(len(docs)):
        if y_t[i] != y_p[i]:
            t_name = get_types_name(y_t[i])
            p_name = get_types_name(y_p[i]) if y_p[i] != -1 else 'undefined'
            filename_prefix = t_name + '-' + p_name
            save_to_file('bad', docs[i], filename_prefix)




# t = parse_type_from_file("C:\\Repos\\innoagency-hack\\classifier\\разрешение\\0945faf3b22f5be4752b955449ab28d6_32.txt")
conn = pymongo.MongoClient('localhost', 27017)
db = conn['innoagencyhack']
coll = db['files']

all_docs = list(coll.find())
# shutil.rmtree('res')
for d in all_docs:
    classify(d)
    save_to_file('res', d)
find_bad_classification(all_docs)
print(calc_score(all_docs))


type_5_51 = [[5, 51], 9]
type_141 = [141, 49]
type_28 = [28, 3]
type_10 = [10, 59]
type_8 = [8, 70]
type_7 = [7, 69]
type_1 = [1, 70]

types_to_check = [type_5_51, type_141, type_28, type_10, type_8, type_7, type_1]

for i in types_to_check:
    count = get_count_by_types(coll, i[0])
    print(count / i[1], get_types_name(i[0]))


conn.close()