# Aid Platform Beta

## Contents

1. [Introduction](#-introduction)
2. [Project Management](#-project-management)
3. [Architecture](#-architecture)
4. [Development Guidelines](#-development-guidelines)
	1. [Make Commits Meaningful](#-make-commits-meaningful)
	2. [Dont Commit to Master](#-dont-commit-to-master)
	3. [Run all tests before pushing](#-run-all-tests-before-pushing)
5. [Contributors](#-contributors)

## <a name="introduction"></a> Introduction

Beta version of the DFID Aid Information Platform

## <a name="project-management"></a> Project Management

The project uses a minimum set of tools to allow us to manage the project.  If you need access to any of these talk to James, Alison or John

- Trello (Product Backlog and Planning) - [Aid Platform Beta Board](https://trello.com/board/aid-platform-beta/510679a1af797fce7b001dd4)
- HipChat (Team Communication)
- Github (Version Control, Issues, Pull Requests) - [Aid Platform Beta](https://github.com/DFID/aid-platform-beta)

## <a name="architecture"></a> Architecture

This is the current architectural vision

<img src="https://raw.github.com/DFID/aid-platform-beta/master/lib/images/architecture.png?login=kouphax&token=ab369cd411268e8e3188ec586921cb85" style="width:100%"/>

The technologies suggested are only theoretical and I expect them to change as and when the components evolve.  For now bare minimum spikes through the system are essential to draw out requirements.

- __Loader Admin__ - The alpha provided configuration through a simple file.  The loader admin will be a more user friendly approach to defining what XML files should be loaded as well as providing an area to add "annotations" to the IATI data.
- __Loader__ - The loader pulls in the IATI XML data and loads it into the data stores of the API and Searcher
- __Searcher__ - Provides faceted, fuzzy searching of loaded IATI data.  This is only available to the Platform and not through the API.  The rationale for doing this is that it is very much geared towards the aid-platform format rather than arbitrary API calls.  We can consider exposing this externally as we go on.  Potentially built on top of ElasticSearch.
- __API__ - IATI Data API built against the working draft of the IATI API Standards.  Unlike the beta this wont be tailored specifically to DFIDs assumptions of the data shape.  Extra work will need to be carried out by the aid platform itself
- __Platform__ - The aid platform will consume the API and generate a static site that can be hosted on any provider.

## <a name="development-guidelines"></a> Development Guidelines

### <a id="make-commits-meaningful"></a> Make commits meaningful

Keep the commit tree tidy and meaningful.  GDS have a a [great style guide for Git](https://github.com/alphagov/styleguides/blob/master/git.md).  Be descriptive - "fixed bugs" is not good enough :)

### <a id="dont-commit-to-master"></a> Dont commit to master

Use short lived feature branches to work in.  When something is ready to be merged into master [open a pull request](https://github.com/DFID/aid-platform-beta/pull/new/master) vis Github to allow the rest of the team to quickly review it before it goes into master.

### <a id="run-all-tests-before-pushing"></a> Run all the tests before pushing

Don't rely on the CI server to make sure you haven't broken anything.  Be sure to run tests prior to committing and pushing code to Github.

## <a id="contributors"></a> Contributors

- John Adams (j-adams@dfid.gov.uk)
- James Hughes (james@yobriefca.se)
- Kyle Davidson (k.davidson@kainos.com)
