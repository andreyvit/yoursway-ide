import re
import sys, os

projectpath = sys.argv[1]
sys.path.insert(0, projectpath)

os.environ['DJANGO_SETTINGS_MODULE'] = 'settings'

from django.conf import settings
settings.TEMPLATE_DEBUG = True
from django.template.loader import LoaderOrigin
from django.template.debug import DebugNodeList
from django.test.client import Client
from django.utils.datastructures import SortedDict

nodes = []

class Fragment: pass
class Source: pass

DebugNodeList.original_render_node = DebugNodeList.render_node 

affect = False

sources = SortedDict()

def get_source(origin):
    if origin in sources:
        return sources[origin]
    else:
        s = sources[origin] = Source()
        s.id = len(sources)
        s.name = origin.name
        s.origin = origin.__class__.__name__
        return s

def guard(inf, id):
    return '<!--# +%s #-->' % id + inf + '<!--# -%s #-->' % id

def nodelist_render_node(self, node, context):
    result = DebugNodeList.original_render_node(self, node, context)
    if affect:
        f = Fragment()
        f.id = len(nodes)
        f.region = node.source[1]
        f.source = get_source(node.source[0])
        nodes.append(f)
        return guard(result, f.id)
    else:
        return result

DebugNodeList.render_node = nodelist_render_node

if __name__ != "__main__":
    print "Do never import this module!"
    sys.exit(1)

if len(sys.argv)<3:
    print "Usage: parser <project_source_path> <absolute_path_to_render>"
    sys.exit(1)

url = sys.argv[2]

handler = Client()
affect = False
clean = handler.get(url).content

affect = True
buzzzz = handler.get(url).content

taint_re = re.compile('<!--# ([-|+])(\d+) #-->')

upto = 0

text = ''
for match in taint_re.finditer(buzzzz): # not overlapping matches
    start, end = match.span()
    if start>upto:
        text += buzzzz[upto:start]
        upto = start
    upto = end
    sign, id = match.group(1), int(match.group(2))
    if sign == '+':
        nodes[id].start = len(text)
    if sign == '-':
        nodes[id].stop = len(text)

assert len(buzzzz) == upto
# just in case previous assertion failed
text += buzzzz[upto:]

assert text == clean # ensure we have the same text

for item in sources.values():
    print item.id, item.origin, item.name
print '#-------'
for item in nodes:
    print item.id, item.region[0], item.region[1], item.source.id, item.start, item.stop
print '#-------'
