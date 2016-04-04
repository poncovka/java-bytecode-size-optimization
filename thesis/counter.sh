#!/bin/bash

pdftotext -f 6 project.pdf
chars=$((`cat project.txt | wc -m`))
echo "Počet znaků:         "$(($chars))
echo "Počet normostran:    "$(($chars / 1800 ))
echo "Diplomová práce:     "$(($chars / 1800 * 100 / 50 ))"% z minima"
echo "Minimální rozsah:    50 normostran (25 vysázených stran)"
echo "Bezny rozsah:        80-100 normostran (40-50 vysázených stran)"
rm project.txt
