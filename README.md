# OEV
is a small Java UI tool to make Lightpainting photos and videos of a timelapse photo sequence.

The 3 types of Lightpainting you can create with OEV:

## Lightpainting Image

![Lightpainting Image sample](/docs_resources/Head_sample.png)

Here the lights of each image are summed into one image

## Lightpainting Video

![Lightpainting video sample](/docs_resources/vidSample.GIF)

The lights of each image are incrementally collected, saving one frame with the intermediate collected lights after
adding one next image

## Light Trailing Video

![Lightpainting video sample](/docs_resources/vidSpecSample.GIF)

Similar to Lightpainting Video, but here the summed lights expire after a userdefined amount of further summed frames

# General Usage Manual

The workflow can abbreviated as following:

- export your timelapse video as PNG sequence (each image must have the same resolution)
- in OEV, select all png files as input files, and select a empty output directory
- choose one of the above mentioned modes 
- (in Lighttrailing mode you have to choose a "light-trail length")
- Hit start and wait. Afterwards you have the resulting images in the chosen output directory

see my website for a more detailed description: http://www.tf-fotovideo.de/oev/
Lightpainting Timelapse How-To Blog article:
http://tf-fotovideo.de/blog/how-to-shoot-lightpainting-timelapse/

# Example Projects

Munich Octoberfest Lightpainting:
https://www.youtube.com/watch?v=KR9BL6i_MgQ

Munich Lightpainting Timelapse
https://www.youtube.com/watch?v=bVFC_1oVLrM

feel free to contact me! info@tf-fotovideo.de

