## Ant Build Generator

Ant build generator is an eclipse plugin that generates a *single build file* for your Java projects.

### Installation of the plugin

Create a plugin jar file from source by importing the project in the eclipse and exporting as plugin jar file or use the checked-in jar file (*under plugin folder*). 
Copy the generated jar file in the plugins directory of your eclipse installation.

Launch the eclipse as (eclipse -clean)

A new menu (ANT Generator) should appear after the Window menu.

### How it works?

The plugin works by identifying the projects' (available in the current workspace) facet.

Current support of facets:
- Java
- EJB
- EAR
- Web

When you click the Ant Generator menu icon, you will be presented with a dialog. This dialog box will list all the faceted projects. 
You can select the projects for which build file has to be generated. 
The plugin would generate one consolidated build file for your projects provided that these projects are linked together.


### Version supported
The plugin was developed on Eclipse Indigo (SR1) and has been tested on this version only. 

