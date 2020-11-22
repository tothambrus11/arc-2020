EESchema Schematic File Version 4
EELAYER 30 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L Connector:Conn_01x08_Female J2
U 1 1 5EE9084A
P 3050 2900
F 0 "J2" H 3078 2876 50  0000 L CNN
F 1 "L" H 3078 2785 50  0000 L CNN
F 2 "Connector_PinSocket_2.54mm:PinSocket_1x08_P2.54mm_Vertical" H 3050 2900 50  0001 C CNN
F 3 "~" H 3050 2900 50  0001 C CNN
	1    3050 2900
	1    0    0    -1  
$EndComp
$Comp
L Connector:Conn_01x08_Female J5
U 1 1 5EE91153
P 4450 2900
F 0 "J5" H 4478 2876 50  0000 L CNN
F 1 "R" H 4478 2785 50  0000 L CNN
F 2 "Connector_PinSocket_2.54mm:PinSocket_1x08_P2.54mm_Vertical" H 4450 2900 50  0001 C CNN
F 3 "~" H 4450 2900 50  0001 C CNN
	1    4450 2900
	1    0    0    -1  
$EndComp
$Comp
L Connector:Conn_01x04_Male J1
U 1 1 5EE93237
P 2800 1700
F 0 "J1" V 2862 1844 50  0000 L CNN
F 1 "M1" V 2953 1844 50  0000 L CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x04_P2.54mm_Vertical" H 2800 1700 50  0001 C CNN
F 3 "~" H 2800 1700 50  0001 C CNN
	1    2800 1700
	0    1    1    0   
$EndComp
$Comp
L Connector:Conn_01x04_Male J3
U 1 1 5EE947C1
P 3550 1700
F 0 "J3" V 3612 1844 50  0000 L CNN
F 1 "M2" V 3703 1844 50  0000 L CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x04_P2.54mm_Vertical" H 3550 1700 50  0001 C CNN
F 3 "~" H 3550 1700 50  0001 C CNN
	1    3550 1700
	0    1    1    0   
$EndComp
$Comp
L Connector:Conn_01x04_Male J4
U 1 1 5EE94D2E
P 4200 1700
F 0 "J4" V 4262 1844 50  0000 L CNN
F 1 "M3" V 4353 1844 50  0000 L CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x04_P2.54mm_Vertical" H 4200 1700 50  0001 C CNN
F 3 "~" H 4200 1700 50  0001 C CNN
	1    4200 1700
	0    1    1    0   
$EndComp
$Comp
L Connector:Conn_01x04_Male J6
U 1 1 5EE95521
P 4800 1700
F 0 "J6" V 4862 1844 50  0000 L CNN
F 1 "M4" V 4953 1844 50  0000 L CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x04_P2.54mm_Vertical" H 4800 1700 50  0001 C CNN
F 3 "~" H 4800 1700 50  0001 C CNN
	1    4800 1700
	0    1    1    0   
$EndComp
$Comp
L power:GND #PWR0101
U 1 1 5EE964F5
P 2500 3200
F 0 "#PWR0101" H 2500 2950 50  0001 C CNN
F 1 "GND" H 2505 3027 50  0000 C CNN
F 2 "" H 2500 3200 50  0001 C CNN
F 3 "" H 2500 3200 50  0001 C CNN
	1    2500 3200
	1    0    0    -1  
$EndComp
Wire Wire Line
	2500 3200 2850 3200
$Comp
L power:+3V3 #PWR0102
U 1 1 5EE96DCF
P 3900 3300
F 0 "#PWR0102" H 3900 3150 50  0001 C CNN
F 1 "+3V3" H 3915 3473 50  0000 C CNN
F 2 "" H 3900 3300 50  0001 C CNN
F 3 "" H 3900 3300 50  0001 C CNN
	1    3900 3300
	1    0    0    -1  
$EndComp
Wire Wire Line
	3900 3300 4250 3300
$Comp
L power:GND #PWR0103
U 1 1 5EE97FC2
P 2700 2050
F 0 "#PWR0103" H 2700 1800 50  0001 C CNN
F 1 "GND" H 2705 1877 50  0000 C CNN
F 2 "" H 2700 2050 50  0001 C CNN
F 3 "" H 2700 2050 50  0001 C CNN
	1    2700 2050
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR0104
U 1 1 5EE986D9
P 3450 2050
F 0 "#PWR0104" H 3450 1800 50  0001 C CNN
F 1 "GND" H 3455 1877 50  0000 C CNN
F 2 "" H 3450 2050 50  0001 C CNN
F 3 "" H 3450 2050 50  0001 C CNN
	1    3450 2050
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR0105
U 1 1 5EE9899E
P 4100 2050
F 0 "#PWR0105" H 4100 1800 50  0001 C CNN
F 1 "GND" H 4105 1877 50  0000 C CNN
F 2 "" H 4100 2050 50  0001 C CNN
F 3 "" H 4100 2050 50  0001 C CNN
	1    4100 2050
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR0106
U 1 1 5EE98D4B
P 4700 2050
F 0 "#PWR0106" H 4700 1800 50  0001 C CNN
F 1 "GND" H 4705 1877 50  0000 C CNN
F 2 "" H 4700 2050 50  0001 C CNN
F 3 "" H 4700 2050 50  0001 C CNN
	1    4700 2050
	1    0    0    -1  
$EndComp
$Comp
L power:+3V3 #PWR0107
U 1 1 5EE9943C
P 2900 2050
F 0 "#PWR0107" H 2900 1900 50  0001 C CNN
F 1 "+3V3" H 2915 2223 50  0000 C CNN
F 2 "" H 2900 2050 50  0001 C CNN
F 3 "" H 2900 2050 50  0001 C CNN
	1    2900 2050
	-1   0    0    1   
$EndComp
$Comp
L power:+3V3 #PWR0108
U 1 1 5EE99CED
P 3650 2050
F 0 "#PWR0108" H 3650 1900 50  0001 C CNN
F 1 "+3V3" H 3665 2223 50  0000 C CNN
F 2 "" H 3650 2050 50  0001 C CNN
F 3 "" H 3650 2050 50  0001 C CNN
	1    3650 2050
	-1   0    0    1   
$EndComp
$Comp
L power:+3V3 #PWR0109
U 1 1 5EE9A544
P 4300 2050
F 0 "#PWR0109" H 4300 1900 50  0001 C CNN
F 1 "+3V3" H 4315 2223 50  0000 C CNN
F 2 "" H 4300 2050 50  0001 C CNN
F 3 "" H 4300 2050 50  0001 C CNN
	1    4300 2050
	-1   0    0    1   
$EndComp
$Comp
L power:+3V3 #PWR0110
U 1 1 5EE9A948
P 4900 2050
F 0 "#PWR0110" H 4900 1900 50  0001 C CNN
F 1 "+3V3" H 4915 2223 50  0000 C CNN
F 2 "" H 4900 2050 50  0001 C CNN
F 3 "" H 4900 2050 50  0001 C CNN
	1    4900 2050
	-1   0    0    1   
$EndComp
Wire Wire Line
	4900 2050 4900 1900
Wire Wire Line
	4700 2050 4700 1900
Wire Wire Line
	4300 2050 4300 1900
Wire Wire Line
	4100 2050 4100 1900
Wire Wire Line
	3450 2050 3450 1900
Wire Wire Line
	3650 2050 3650 1900
Wire Wire Line
	2900 2050 2900 1900
Wire Wire Line
	2700 2050 2700 1900
Wire Wire Line
	2600 1900 2600 2800
Wire Wire Line
	2600 2800 2850 2800
Wire Wire Line
	2800 1900 2800 2900
Wire Wire Line
	2800 2900 2850 2900
Wire Wire Line
	4600 1900 4600 2900
Wire Wire Line
	4800 1900 4800 3000
Wire Wire Line
	4600 2900 4250 2900
Wire Wire Line
	4250 3000 4800 3000
Wire Wire Line
	4000 1900 4250 3100
Wire Wire Line
	3550 3000 3550 1900
Wire Wire Line
	2850 3000 3550 3000
Wire Wire Line
	3350 1900 3350 3100
Wire Wire Line
	3350 3100 2850 3100
$Comp
L Mechanical:MountingHole_Pad H1
U 1 1 5EEB3772
P 4100 4000
F 0 "H1" H 4200 4049 50  0000 L CNN
F 1 "MountingHole_Pad" H 4200 3958 50  0000 L CNN
F 2 "Connector_Pin:Pin_D1.3mm_L11.0mm" H 4100 4000 50  0001 C CNN
F 3 "~" H 4100 4000 50  0001 C CNN
	1    4100 4000
	1    0    0    -1  
$EndComp
$Comp
L Mechanical:MountingHole_Pad H2
U 1 1 5EEB3C51
P 4350 4000
F 0 "H2" H 4450 4049 50  0000 L CNN
F 1 "MountingHole_Pad" H 4450 3958 50  0000 L CNN
F 2 "Connector_Pin:Pin_D1.3mm_L11.0mm" H 4350 4000 50  0001 C CNN
F 3 "~" H 4350 4000 50  0001 C CNN
	1    4350 4000
	1    0    0    -1  
$EndComp
$Comp
L Mechanical:MountingHole_Pad H3
U 1 1 5EEB3D34
P 4600 4000
F 0 "H3" H 4700 4049 50  0000 L CNN
F 1 "MountingHole_Pad" H 4700 3958 50  0000 L CNN
F 2 "Connector_Pin:Pin_D1.3mm_L11.0mm" H 4600 4000 50  0001 C CNN
F 3 "~" H 4600 4000 50  0001 C CNN
	1    4600 4000
	1    0    0    -1  
$EndComp
$Comp
L Mechanical:MountingHole_Pad H4
U 1 1 5EEB3F94
P 4850 4000
F 0 "H4" H 4950 4049 50  0000 L CNN
F 1 "MountingHole_Pad" H 4950 3958 50  0000 L CNN
F 2 "Connector_Pin:Pin_D1.3mm_L11.0mm" H 4850 4000 50  0001 C CNN
F 3 "~" H 4850 4000 50  0001 C CNN
	1    4850 4000
	1    0    0    -1  
$EndComp
Wire Wire Line
	4200 3200 4250 3200
Wire Wire Line
	4200 1900 4200 3200
$EndSCHEMATC