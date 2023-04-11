# Greenhouse SMOL controller

This code provides a representation for the different assets we expect in the greenhouse:
- Greenhouse 
- Pot
- Shelf
- Pump

For each asset a class is defined

### Greenhouse (no fields)

<br>

### Shelf

> In each `Shelf` there are two **group of plants**, each one composed by **two plants**. In particular we track the `Pot` in which the plants are instead of the plant itself.<br>
> Each `Shelf` is indentified by its "floor" (`shelfFloor`)

<br>

### Pot
> `Pot` is a container for a plant. <br>
> It is indentified by
> 1. The shelf in which it is located (`shelfFloor`)
> 2. The position of the pot group in the `Shelf` (`groupPosition`). Can be left or right. <br>
> 3. The position of the pot in the group (`potPosition`). Can be left or right.

<br>

### Pump

> `Pump` represents a pump used to water a group of pots. <br>
> For each group of pots there is a pump. So it is identified by:
> 1. Its shelf (`shelfFloor`)
> 2. The watered pot group position on the `Shelf` (`groupPosition`)

<br>