import re

msg = "<01:v:1245>|<2:p:12743>|<3:v:7654>|<4:v:23764>|<5:v:8634>"

matchList = re.findall(r'<(\d+):([vsp]):(\d+)>', msg, re.M|re.I)


match1 = matchList[0]

#print match1
#print match1[0]

msg2 = "<distanceSensor1:234><distanceSensor2:543>"
matchList2 = re.findall(r'<([A-Za-z_]\w+):(\d+)>', msg2, re.M|re.I)
print matchList2