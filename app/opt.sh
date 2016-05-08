#!/bin/bash

# Author: Vendula Poncova
# Date: 30.4.2016
# Optimize the given class.
#
# Parameters:
#   input   A .class file.

# setup input, output
input=$1 
outdir=$( mktemp --directory )
output=$outdir/$(basename $input)

mkdir --parents $outdir

# settings
print_args="--print --pool --debug"

# optimize input
jbyco -o $outdir $input

# compare input and output
diff -y <( jbyca $print_args "$input" ) <( jbyca $print_args "$output" )

# print the sizes and their subtraction
size_before=$( ls -l $input | awk '{print $5}' )
size_after=$( ls -l $output | awk '{print $5}' )


echo "Size in bytes before: ${size_before}"
echo "Size in bytes after:  ${size_after}"
echo "Saved bytes:          $((size_before - size_after))"
echo "Saved percents:       $(((size_before - size_after) * 100 / size_before))%"

# remove the temporary files
rm -r $output
