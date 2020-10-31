import re
from pathlib import Path

from constants import *


def get_count_by_types(collection, types):
    try:
        or_query = []
        for i in types:
            or_query.append({"type_id": i})
        query = {"$or": or_query}
    except:
        query = {"type_id": types}

    return collection.find(query).count()


def get_types_name(types):
    try:
        return ", ".join(map(lambda x: TYPES[x], types))
    except:
        return TYPES[types]


def get_type(doc):
    doc = re.sub(r'\s|\n|\t', ' ', doc.lower())
    matches = []
    for k, v in TYPE_REGEX:
        iters = list(re.finditer(k, doc))
        if iters:
            matches.append((min([m.start(0) for m in re.finditer(k, doc)]), v))

    if len(matches) == 0:
        return '', -1

    first_match = min(matches, key=lambda x: x[0])
    return TYPES[first_match[1]], first_match[1], first_match[0]


# def get_type(doc):
#     doc = re.sub(r'\s|\n|\t', ' ', doc.lower())
#     for k, v in TYPE_REGEX:
#         if re.search(k, doc):
#             return TYPES[v], v
#
#     return '', -1


def save_to_file(base_dir, doc, filename_prefix=''):
    type_name = doc["type"] if doc["type_id"] != -1 else "undefined"
    type_name = base_dir + "/" + type_name
    filename = filename_prefix + doc["_id"] + ".txt"
    path = "{0}/{1}".format(type_name, filename)
    Path(type_name).mkdir(parents=True, exist_ok=True)
    with open(path, "wb+") as f:
        f.write(str.encode(doc["text"], 'utf8', errors='replace'))


def get_doc_label(path):
    paths = path.split('/')
    filename = paths[-1].lower()
    if filename in LABELS:
        return LABELS[filename]

    file_dir = paths[-2]
    return DIR_LABELS[file_dir]
