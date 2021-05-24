#!/bin/bash

#set -x

NUM_REQUESTS=${NUM_REQUESTS:-300}
CONCURRENCY=${CONCURRENCY:-30}
#HOST='${HOST}'
HOST=https://address-poc-address-poc.apps.integration-non-production.ds.ahunga.co.nz/

# this is pvc (tmp/data) and git repo (performanceresults)
OUT_DIR=/tmp/data/performanceresults/`date +%Y-%m-%d-%H-%M-%S`
mkdir -p ${OUT_DIR}

bold=$(tput bold)
normal=$(tput sgr0)

echo "${bold}POST 200 /graphql/address${normal}"

hey -o csv -t 30 -n ${NUM_REQUESTS} -c ${CONCURRENCY} -H  "Content-Type: application/json;charset=utf-8" -m POST -d '{"query": "{ddress(size: 10, search: \"23 crank street\") {address}}"}' $HOST/graphql > ${OUT_DIR}/address-200.perfout

hey -t 30 -c 10 -n 100 -H  "Content-Type: application/json;charset=utf-8" -m POST -d '{"query": "{ddress(size: 10, search: \"23 crank street\") {address}}"}' $HOST/graphql | grep % | awk '{print $1,"",$3}' | sed -r 's/%//g' | sed '1 i\percentile response' > ${OUT_DIR}/address-200-percentile.percout

#echo "${bold}POST 200 /mobileapi/api2/banking/is-payid-available/${normal}"
#
#ab ${VERBOSITY} -n ${NUM_REQUESTS} -c ${CONCURRENCY} -H "Content-Type: application/json" -H "Accept: application/json" -H "api_key: 4c98a489840254ed2097781bf56c1e61" -T 'application/json' -g ${OUT_DIR}/is-payid-available-200.out -p is-payid-available-200 "${HOST}/mobileapi/api2/banking/is-payid-available" 2>&1 | tee ${OUT_DIR}/is-payid-available-200.ab
#
#echo "${bold}POST 200 /mobileapi/api2/banking/get-existing-payids/${normal}"
#
#ab ${VERBOSITY} -n ${NUM_REQUESTS} -c ${CONCURRENCY} -H "Content-Type: application/json" -H "Accept: application/json" -H "api_key: 4c98a489840254ed2097781bf56c1e61" -T 'application/json' -g ${OUT_DIR}/get-existing-payid-200.out -p get-existing-payid-200 "${HOST}/mobileapi/api2/banking/get-existing-payids" 2>&1 | tee ${OUT_DIR}/get-existing-payid-200.ab

# generate plot data
cp perf.plot.gnu ${OUT_DIR}/
cp perc.plot.gnu ${OUT_DIR}/
cd ${OUT_DIR}

ls *.perfout | sed -e "s/.perfout//" > list
for i in `cat list` ; do
   sed -e "s/INPUTFILE/$i/" -e "s/OUTPUTFILE/$i/" \
   perf.plot.gnu | gnuplot
done
rm list

ls *.percout | sed -e "s/.percout//" > list
for i in `cat list` ; do
   sed -e "s/INPUTFILE/$i/" -e "s/OUTPUTFILE/$i/" \
   perc.plot.gnu | gnuplot
done
rm list
