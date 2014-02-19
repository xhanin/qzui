#! /bin/sh
if [ -n "$1" ]; then
  mkdir tmp
  curl -L https://github.com/twbs/bootstrap/releases/download/v$1/bootstrap-$1-dist.zip -o tmp/bootstrap.zip
  rm -fr app/lib/bootstrap
  unzip tmp/bootstrap.zip -d app/lib
  mv app/lib/dist app/lib/bootstrap
else
  echo "Usage: update-bootstrap <version>"
fi  
