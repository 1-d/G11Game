# stability is not guaranteed

import sys
sys.stdin=open("1.txt")
sys.stdout=open("undo.txt","w")

temp = []       # info about enemy

out=[]

lines = sys.stdin.read().split("\n")[4:]

index=0
while index<len(lines):
    n=int(lines[index])
    col = [0]*12
    extra = []
    for i in lines[index+1:index+n+1]:
        l=list(map(int,i.split()))
        if l[0]==1:
            col[l[1]]=1
        else:
            extra.append(":".join(map(str,l)))
    extra.insert(0,"".join(map(str,col)))
    print(" ".join(extra))
    index+=n+1


