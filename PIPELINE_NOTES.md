# Pipeline notes

## Requerimientos de la imagen
- Reglas de workflow: no crear pipeline en push; ejecutar en merge request.
- Permitir ejecucion por tags para limitar escenarios.
- Permitir seleccionar ambiente (DES, QA, STG).
- Stages: descarga de dependencias, ejecucion de pruebas, publicacion de reporte, cierre.
- Artifacts de reporte con retencion maxima en dias.

## Extraido del GitLab CI del banco
- Reglas de workflow para bloquear push y permitir MR/manual.
- Variables con opciones para tag y ambiente.
- Cache de .m2/repository y stage de dependencias.
- expire_in en artifacts de reportes.

## Mapeo de ambiente usado en CI
- ENVIRONMENT=DES mapea a -DEnvironment=DEV.
- ENVIRONMENT=QA mapea a -DEnvironment=QA1.
- ENVIRONMENT=STG mapea a -DEnvironment=UAT.

## Resultados de investigacion
### Edge en Linux
- Microsoft Edge esta disponible para Linux (deb/rpm).
- Requiere el binario del navegador; WebDriverManager solo baja el driver.
- En contenedores, instalar microsoft-edge-stable desde el repo de Microsoft o usar una imagen custom.
- Headless es compatible; usar flags de headless tipo chromium para Edge.
- Si Edge es necesario en CI, extender la imagen base y configurar BROWSER=EDGE.

### Font Liberation para headless
- fonts-liberation agrega fuentes comunes para evitar glifos faltantes y cambios de layout.
- Recomendado para render consistente en CI headless.
- El pipeline instala fonts-liberation en el job de test; una imagen custom puede incluirlo.

### Podman
- Podman es un runtime de contenedores, util cuando Docker no esta permitido.
- No es requerido para este pipeline porque las pruebas corren en el runner de GitLab.
- Solo se necesita si el runner usa Podman o si se construyen/publican imagenes.

### Deploy tokens
- Usar deploy tokens de GitLab con read_package_registry para Maven.
- No commitear tokens; configurar variables CI enmascaradas/protegidas.
- .m2/settings.xml espera:
  - MAVEN_USERNAME_COMMONS_DATA / MAVEN_PASSWORD_COMMONS_DATA
  - MAVEN_USERNAME_CONNEXUSDB / MAVEN_PASSWORD_CONNEXUSDB
  - MAVEN_USERNAME_CONNEXUSDB_GUI / MAVEN_PASSWORD_CONNEXUSDB_GUI
  - MAVEN_USERNAME_CONNEXUSDB_PLUGIN / MAVEN_PASSWORD_CONNEXUSDB_PLUGIN

## Configuracion de .m2
- El repo local de Maven se define en .m2/repository y se cachea entre jobs.
- .m2/repository esta en gitignore para evitar binarios en el repo.

## Documentacion del .gitlab-ci.yml agregado
### Workflow
- Bloquea pipelines en push y permite ejecucion en merge request y manual (Run pipeline/API).

### Variables
- ENVIRONMENT: permite seleccionar DES/QA/STG, con mapeo interno a DEV/QA1/UAT.
- BROWSER: define navegador (CHROME/FIREFOX/EDGE).
- TAGS: expresion de tags de Cucumber (vacio ejecuta todo).
- MAVEN_REPO_LOCAL/MAVEN_OPTS/MAVEN_CLI_OPTS: controlan el repo local y opciones de Maven.

### Stages
- dependencies: descarga/resolve de dependencias Maven.
- test: ejecucion de pruebas con tags y ambiente.
- report: validacion y publicacion del reporte.
- close: cierre del pipeline con mensaje final.

### Jobs
- dependencies_job: resuelve dependencias y cachea .m2/repository con artifacts 1 dia.
- run_tests: instala chromium y fonts-liberation, ejecuta mvn con runPipeline, Environment, Browser y tags.
- generate_report: valida reports/SparkReport.html y expone artifact por 7 dias.
- close_job: paso final informativo.

### Artifacts y cache
- cache global de .m2/repository.
- artifacts del reporte con expire_in de 7 dias.
