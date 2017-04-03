#PrempointQualifier-KevinCitron

Android Studio 2.3

This project is an Android mobile app. The specs were supplied by Paul Ward at Prempoint,
as a way of demonstrating, some of my Android mobile development capabilities.

The Android mobile App that I have written does the followwing.

* Create a single activity app with a fragment for the UI
* Show a single button at the top right, background color #00abfb, rounded corners, that initially says “Start Scan” in white letters 

Tapping the button will do the following 
  
  * Change button background to #c5d772, text to “Scanning”
  * text color #ececed, and make it not tappable
  * Start a BLE scan (please implement BLE scanner in a service that returns results via local broadcast).
	   
     Results should be shown in a ListView in the fragment showing
    
    
     * Device’s bluetooth name
     * RSSI signal strength
     * Hex representation of the scan record


 * After 15 seconds, the scan will stop and the button will be 
  changed back to “Start Scan” and enabled.
 
 * Tapping again will clear the ListView results and start over.
  
  ---------------------------------------------------------------------------------------------------------
  In addition to fulfilling the spec. I have added skinning and various other stylings to
  core, Android components.
  
  I also, felt, that some sort of visual feedback was in order, during the 15 second scan period. 
  To accomplish the visual feeback, I created a TimerTask method, which alternates the ScanButton text
  color, from a medium gray, to red. Which gives the user the appearence, that, some activity is taking 
  place in the background.
  
  Kevin Citron
