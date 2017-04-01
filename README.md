# PrempointQualifier-KevinCitron

This is am Android mobile app, that does the following.
Thanks again for your time today, it was a pleasure talking with you.  As promised, here are the requirements for the sample app:

	•	Store code in github and send me the link 
	•	Create a single activity app with a fragment for the UI 
	•	Show a single button at the top right, background color #00abfb, rounded corners, that initially says “Start Scan” in white letters 
	•	Tapping the button will do the following: 
	◦	Change button background to #c5d772, text to “Scanning”, text color #ececed, and make it not tappable 
	◦	Start a BLE scan (please implement BLE scanner in a service that returns results via local broadcast) 
	◦	Results should be shown in a ListView in the fragment showing: 
	▪	Device’s bluetooth name 
	▪	RSSI signal strength 
	▪	Hex representation of the scan record 

	◦	After 15 seconds, the scan will stop and the button will be changed back to “Start Scan” and enabled 
	◦	Tapping again will clear the ListView results and start over
	◦	
  It was developede to demonstrate my development capabilities to Prempoint Inc.
  
  In addition to the requirement, I added skinning to various stock Android widgets, as well, as,
  visual feedback during the 15 second scan delay. Button text, which alternates, between two colors,
  set, by a timertask function, every 1.5 seconds. giving the effect of a blinking button.
	◦	
	◦	
	
