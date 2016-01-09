#!/bin/bash

pdftotext -f 6 project.pdf
chars=$((`cat project.txt | wc -m`))
echo "Počet znaků:         "$(($chars))
echo "Počet normostran:    "$(($chars / 1800 ))
echo "Semestralni projekt: "$(($chars / 1800 * 100 / 20 ))"% z minima"
echo "Diplomová práce:     "$(($chars / 1800 * 100 / 50 ))"% z minima"
echo "Minimální rozsah:    20 normostran (10 vysázených stran)"
echo "Bezny rozsah:        30-40 normostran (15-20 vysázených stran)"
rm project.txt
