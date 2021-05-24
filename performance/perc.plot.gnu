set terminal png
set datafile separator whitespace
set datafile missing "NaN"
set output "OUTPUTFILE.png"
set title 'OUTPUTFILE'
set xlabel "percentile [%]"
set ylabel "response [s]"
set tics nomirror
set grid
plot "INPUTFILE.percout" using 1:2 with lines title "response"
