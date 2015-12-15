#!/bin/bash

pdftotext -f 6 project.pdf
chars=$((`cat project.txt | wc -m`))
echo "Bezny rozsah: 30-40 normostran."
echo "Aktualni pocet normostran v projektu:" $(($chars / 1800 ))
echo "Procentualne hotovo:" $(($chars / 1800 * 100 / 80 )) "%"
echo "Semestralni projekt:" $(($chars / 1800 * 100 / 30 )) "%"
echo "Počet znaků:" $(($chars))
rm project.txt
