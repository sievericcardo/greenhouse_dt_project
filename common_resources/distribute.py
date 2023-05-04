import os
import shutil

"""
This script copies the common files needed in multiple directories to work.
They are copied instead of liked from a sigle source 
    because single directories are meant to be separate project independent from each other.
For now it copies:
    - greenhouse.ttl
    - greenhouse.smol
into the following directories:
    - smol_runner\src\main\resources
    - smol_run

Use:
    If you need to edit one of the common files, edit the file in common_resources directory.
    Then run this script to copy the edited file to every destination directory.
"""


# Define the source and destination directories
common_resources_dir = "common_resources"
destination_dirs = ["smol_runner\src\main\\resources", "smol_run"]

# Define the list of files to copy
files_to_copy = ["greenhouse.ttl", "greenhouse.smol"]

# Iterate over the list of files and directories and copy files from source to every destination directory
for file in files_to_copy:
    source_path = os.path.join(common_resources_dir, file)
    for destination_dir in destination_dirs:
        destination_path = os.path.join(destination_dir, file)
        shutil.copy2(source_path, destination_path)
        print("Copied {} to {}".format(source_path, destination_path))
