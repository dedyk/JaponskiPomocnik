#!/bin/bash

current_full_dir=`pwd`
current_dir=`basename $current_full_dir`

package_name=${current_dir}_`date "+%Y%m%d_%H%M"`

tar cf - ../$current_dir | bzip2 -9 -f > ../$package_name.tar.bz2

echo ""
echo $package_name
