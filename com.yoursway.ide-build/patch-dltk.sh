#!/bin/sh

PATCHDIR=$1
DESTDIR=$2

(cd "$DESTDIR"; find . -mindepth 1 -maxdepth 1 -type d) | while read plugin; do

  #Strip ./
  plugin=$(basename $plugin)

  echo "Checking $plugin"
  if [ -f "$PATCHDIR/$plugin.patch" ]; then
    echo "Patch found. Patching."
    (cd "$DESTDIR/$plugin"; patch -p0 < $PATCHDIR/$plugin.patch) || PATCH_BROKE=1
  fi
done

if [ -n "$PATCH_BROKE" ]; then
  exit 1
fi
