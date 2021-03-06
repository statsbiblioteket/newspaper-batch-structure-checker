1.9
* Use newest version of item event framework. No functional changes for this module.
* Configuration has been extended and changed and example config has been updated. Please update your configuration files.

1.8
* Update to version 1.10 of batch event framework
* Update to version 1.10 of mfpak integration 

1.7
* Add support for check of partial edition dates

1.6
* Edition date checking now supports overlapping MFPak dates.

1.5
* Update check for ISO-FILM-target: Require the directory, but allow it to be empty
* Update error message in mfpak date checks to be clearer

1.4
* Update to newspaper-parent 1.2
* Add unit tests for missing pages
* Update to version 1.6 of batch event framework
* Update to version 1.5 of newspaper mfpak integration

1.3
* Updated to newspaper-parent 1.1, supporting new test strategy
* Use new tree iterator
* Only read SQL database information once
* Support new empty pages change request

1.2.3
* Updated to newspaper-batch-event-framework 1.4.6.
* Parameter autonomous.component.maxResults is now supported.

1.2.2
* Remove the last use of System.out.println()

1.2.1
* Update to newpaper-batch-event-framework 1.4.2, to make the component quiet on stderr

1.2
- Update to newspaper-batch-event-framework 1.4
- Update to mfpak-integration 1.3
- Add support for fuzzy dates

1.1
- Batch event framework to version 1.1 as 1.0 used invalid DOMS dependencies
- Rework of the config files to remove redundancy, among other things
- Check for option B1/B2/B9 existence of ALTO files
- Bugfix: Allow more than one film per batch
- Use newest batch event framework 1.2

1.0
- MFPak database related checks
- File names are now referred correctly in the QA report
- All checks refer to specifications
- Batch structure xml stored in DOMS or in file
- Film Id can be any length of numbers
- All sequence numbers are now checked

0.2
Simple file structure checks converted to Schematron.
All checks done, except:
  MFPak database related checks
  Numbering sequence checks (page image numbering checks are implemented though).

0.1
Initial release

