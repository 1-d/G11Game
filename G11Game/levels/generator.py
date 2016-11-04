file1 = input("Enter input file (including extension):")
file2 = input("Enter output file (including extension):")

import sys
sys.stdin=open(file1)
sys.stdout=open(file2,"w")

import sys
sys.stdout=open(file2,"w")

print("20 40 5000")
print()

lines=open(file1).read().strip().split('\n')

n=len(lines)

print(n)
print()

# load a dictionary

d={"1":"1 %d"}

for i in range(n):
    things=[]
    line=lines[i].split()
    for j in range(len(line[0])):
        if line[0][j]=="1":
            things.append(d[line[0][j]]%j)
    for obj in line[1:]:
        things.append(obj.replace(":"," "))
    print(len(things))
    print("\n".join(things))

