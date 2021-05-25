set terminal png
set datafile separator ","
set datafile missing "NaN"
set output "OUTPUTFILE.png"
set title 'OUTPUTFILE'
set xlabel "request [#]"
set ylabel "time [s]"
set tics nomirror
set grid
plot "INPUTFILE.perfout" using (column(0)):($7==200?$1:NaN) with lines title "response-time", \
"INPUTFILE.perfout" using (column(0)):($7==200?$2:NaN) with lines title "DNS+dialup", \
"INPUTFILE.perfout" using (column(0)):($7==200?$5:NaN)  with lines title "Response-delay"
