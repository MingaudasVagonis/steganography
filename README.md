# Steganography

## *Steganographically encoding bitmaps*

## Example 
<img src="exmp/stenago_logo.png" alt="logo" width="150px" height="150px"/>

[**Play store**](https://play.google.com/store/apps/details?id=com.mtr.stegano)

## Data can be encoded in two different ways: scrambled and linear

### Scrambled

The data is broken down into pieces and scrambled randomly across the source bitmap, 
the starting point is also determined randomly. 

Each character is encoded as it's 16bit representation and 31bit representation of next character's position.

### Linear 

The data is encoded as a sequence

Each character is encoded as it's 16bit representation.
