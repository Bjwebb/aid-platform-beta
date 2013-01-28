# Aid Platform Beta

## Contents

1. [Introduction](#introduction)
2. [Development Guidelines](#development-guidelines)
	1. [Make Commits Meaningful](#make-commits-meaningful)
	2. [Dont Commit to Master](#dont-commit-to-master)
	3. [Run all tests before pushing](#run-all-tests-before-pushing)
3. [Contributors](#contributors)

## <a id="introduction"></a> Introduction

Beta version of the DFID Aid Information Platform

## <a id="development-guidelines"></a> Development Guidelines

### <a id="make-commits-meaningful"></a> Make commits meaningful

Keep the commit tree tidy and meaningful.  GDS have a a [great style guide for Git](https://github.com/alphagov/styleguides/blob/master/git.md).  Be descriptive - "fixed bugs" is not good enough :)

### <a id="dont-commit-to-master"></a> Dont commit to master

Use short lived feature branches to work in.  When something is ready to be merged into master [open a pull request](https://github.com/DFID/aid-platform-beta/pull/new/master) vis Github to allow the rest of the team to quickly review it before it goes into master.

### <a id="run-all-tests-before-pushing"></a> Run all the tests before pushing

Don't rely on the CI server to make sure you haven't broken anything.  Be sure to run tests prior to committing and pushing code to Github.

## <a id="contributors"></a> Contributors

- John Adams
- James Hughes