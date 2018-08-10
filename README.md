android Phone/sms application with large buttons. Phone and sms numbers are defined in a config file DCIM/PHONE/phone_list.txt

Example of the config file:
<pre>
## phones: p n name phone_number 
p 1 Name1 8....678901
p 2 Name2 8....678902
p 3 Name3 8....678907
p 4 Name4 8....678904
## call buttons: c n phone color
c 1 1 #d3ffce
c 2 2 #d3ffce
c 3 3 #c6e2ff
c 4 4 blue
## sms buttons: s n phone1,phone2,... color label sms_message 
s 1 4 blue   1 a
s 2 4 orange 2 b
s 3 4 red    SOS !SOS
</pre>
