from urllib.request import urlopen
from urllib.error import HTTPError
import sys


ABOUTHELP = """YGet 0.1b, network receiver
Usage: yget [OPTION] [URL]...

Startup without flags:
    $ yget URL1 URL2 URL3 ...

Startup with flags:
    -f    specify the text file with URL's list

          Structure of the text file with URL's list:
              URL1
              URL2
              URL3
              ...
    
        $ yget -f path_to_text_file        
"""


# color text in the console
class TextColors:
    ENDCOLOR = '\033[0m'
    REDCOLOR = '\033[31m'
    REDBGCOLOR = '\033[41m'
    GREENCOLOR = '\033[92m'
    BLUECOLOR = '\033[94m'
    BOLDTEXT = '\033[1m'
    UNDERLINETEXT = '\033[4m'


# statistic counters
errorCount = 0
okCount = 0


# downloader files from internet
def get_file_from_net(urlList):

    if urlList.__len__() <= 0:
        return

    print(
        TextColors.BLUECOLOR +
        TextColors.BOLDTEXT +
        TextColors.UNDERLINETEXT +
        "START:" +
        TextColors.ENDCOLOR
    )

    for url_item in urlList:
        global okCount
        global errorCount
        try:
            obj_url_open = urlopen(url_item)
            file_name = url_item.split('/')[-1]
            obj_file = open(file_name, "wb")
            obj_file.write(obj_url_open.read())
            obj_file.close()
            okCount += 1
            print(TextColors.GREENCOLOR + TextColors.BOLDTEXT + "OK" + TextColors.ENDCOLOR, url_item)
        except HTTPError as e:
            errorCount += 1
            print(TextColors.REDCOLOR + TextColors.BOLDTEXT + "ERROR:" + TextColors.ENDCOLOR, url_item)


# parsing file with URL list
def load_url_from_file(fileName):
    with open(fileName) as urlFiles:
        fileLines = urlFiles.readlines()
    fileLines = [x.strip() for x in fileLines]
    return fileLines


# parsing command line arguments
arguments = []
argIndex = 1
while argIndex < len(sys.argv):
    arguments.append(sys.argv[argIndex])
    argIndex += 1


# processing
if arguments.__len__() == 0: # without arguments
    print(ABOUTHELP)
elif arguments.__len__() == 1: # single URL
    get_file_from_net(arguments)
elif arguments.__len__() >= 2: # many arguments
    if arguments[0] == "-f": # download list in the specify file
        list_urls = load_url_from_file(arguments[1])
        get_file_from_net(list_urls)
    else: # many URL's
        get_file_from_net(arguments)


# display statistic
if okCount > 0 or errorCount > 0:
    print(
        TextColors.BLUECOLOR +
        TextColors.BOLDTEXT +
        TextColors.UNDERLINETEXT +
        "FINISH" + TextColors.ENDCOLOR +
        TextColors.BLUECOLOR +
        "\nDownloaded: " + str(okCount) +
        "\nErrors: " + str(errorCount) +
        TextColors.ENDCOLOR
    )