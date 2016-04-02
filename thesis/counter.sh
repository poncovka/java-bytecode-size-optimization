#!/bin/bash

pdftotext -f 6 project.pdf
chars=$((`cat project.txt | wc -m`))
echo "Počet znaků:         "$(($chars))
echo "Počet normostran:    "$(($chars / 1800 ))
echo "Diplomová práce:     "$(($chars / 1800 * 100 / 50 ))"% z minima"
echo "Minimální rozsah:    ? normostran (? vysázených stran)"
echo "Bezny rozsah:        ?-? normostran (?-? vysázených stran)"
rm project.txt
