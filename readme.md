Compare jar files
-----------------
#HandleBarFile 2heck.bar baseline.bar
java -classpath "%CLASSPATH%;C:\Users\blmm_m\IBM\ACET11\workspace\AceTooling\out\artifacts\handleBar\handleBar.jar" com.id.ace.bar.HandleBarFile C:\Matthias\temp\archive\GenericInternalSSHMove_PROD.bar C:\Matthias\temp\archive\GenericInternalSSHMove_TEST.bar



Run environment
-----------------
#GenerateOverview PSAAEDIIIBTEST D:\IBM\mqsi\Nodes\components PSAAEDIMQTEST
qmgrName = args[2];
java -classpath "%CLASSPATH%;./handleBar.jar" com.id.ace.environment.overview.GenerateOverview PSAAEDIIIBTEST D:\IBM\mqsi\Nodes\components PSAAEDIMQTEST
