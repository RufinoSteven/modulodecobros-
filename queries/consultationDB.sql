#QryCustomerFinancialSummary
select CUNTID IDENTIFICACION,CASE WHEN(dmtyp) = '1' THEN 'Ahorro' else 'Corriente' END as Tipo_cuenta,
cp9.CUXREL RELACION, ln.dmacct CUENTA,
CASE WHEN(dmstat) = 1 THEN 'Activa'
     WHEN(dmstat) = 3 THEN 'DB no permitidos'
     WHEN(dmstat) = 5 THEN 'Inactiva'
     WHEN(dmstat) = 8 THEN 'Fallecido'
     WHEN(dmstat) = 4 THEN 'Cerrada'
     WHEN(dmstat) = 7 THEN 'Cerrada al posteo'
     END AS ESTADO, (select CASE WHEN (select sum(amount_of_stop_hold) from bdrqcd.tap003sq where account_number in (acct.account_nbr)) is null THEN dmcbal
       ELSE dmcbal - (select sum(amount_of_stop_hold) from bdrqcd.tap003sq where account_number in (acct.account_nbr)) END AS BALANCE
from bdrqcd.tap00201 acct
where acct.account_nbr in (ln.dmacct))
from  BDRQCD.CUP00901 cp9
inner join BDRQCD.CUP00301 cp3 on cp3.CUNBR = cp9.CUX1CS
inner join bdrqcd.tap00201 ln  on cux1ac in ((10000000000 + dmacct) , (60000000000 + dmacct))
where CUNTID =? or TAX_ID_NUMBER ='RNC'


#QryPendingEntries
SELECT * FROM bdrdatos.cob010pf
INNER JOIN bdrdatos.cob011pf
ON c10numprod =c11numprod
INNER JOIN BDRQCD.TAP00201
ON c11nucuco =account_nbr
WHERE dmtyp =?
AND dmstat =?
AND dmcbal <=0
AND c10tipcob =?
FETCH FIRST ? ROWS ONLY

#QryCommissionDebtAndPendingAmountById
SELECT c10vacoor, c10valcob, c10vacope FROM bdrdatos.cob010pf
INNER JOIN bdrdatos.cob011pf ON c10numprod =c11numprod
INNER JOIN BDRQCD.TAP00201 ON c11nucuco =account_nbr
WHERE dmtyp = 1 AND dmstat = 1 AND dmcbal >=0 AND c10numprod = ?

#QryPendingComissions
SELECT
    cob.*,
    tap.account_nbr,
    tap.currency_code,
    iden.identificacion
FROM bdrdatos.cob010pf cob
         INNER JOIN bdrdatos.cob011pf c11 ON cob.c10numprod = c11.c11numprod
         INNER JOIN BDRQCD.TAP00201 tap ON c11.c11nucuco = tap.account_nbr
         LEFT JOIN (
    SELECT
        cp9.CUX1AC,
        MIN(cp3.CUNTID) AS identificacion
    FROM BDRQCD.CUP00901 cp9
             INNER JOIN BDRQCD.CUP00301 cp3 ON cp3.CUNBR = cp9.CUX1CS
    WHERE cp3.CUNTID <> ''
    GROUP BY cp9.CUX1AC
) iden ON iden.CUX1AC IN ((10000000000 + tap.account_nbr), (60000000000 + tap.account_nbr))
WHERE tap.dmtyp = 1
  AND tap.dmstat = 1
  AND tap.dmcbal <= 0
  AND tap.currency_code = 0
  AND cob.c10tipcob = ?
  AND cob.c10estcob = ?
FETCH FIRST ? ROWS ONLY

#QryGetAccountActualFirstChargeDetails
SELECT
    cob.c10numprod,
    cob.c10tipcob,
    cob.c10estcob,
    cob.C10vacoor,
    cob.c10vacope,
    cob.c10valcob,
    cob.c10descri,
    cob.c10fecori,
    cob.c10feveco,
    tap.account_nbr,
    tap.currency_code,
    iden.identificacion
FROM bdrdatos.cob010pf cob
INNER JOIN bdrdatos.cob011pf c11 ON cob.c10numprod = c11.c11numprod
INNER JOIN BDRQCD.TAP00201 tap ON c11.c11nucuco = tap.account_nbr
LEFT JOIN (
SELECT cp9.CUX1AC, MIN(cp3.CUNTID) AS identificacion
FROM BDRQCD.CUP00901 cp9
INNER JOIN BDRQCD.CUP00301 cp3 ON cp3.CUNBR = cp9.CUX1CS
WHERE cp3.CUNTID <> '' GROUP BY cp9.CUX1AC
) iden ON iden.CUX1AC IN ((10000000000 + tap.account_nbr), (60000000000 + tap.account_nbr))
WHERE c11.c11nucuco = ?
ORDER BY cob.c10feveco ASC, cob.c10numprod ASC
FETCH FIRST 1 ROW ONLY

#QryCandidateGenerator
SELECT
    outer_query.c10numprod      AS CANDIDATE_COLLECTION_ID,
    outer_query.c10tipcob       AS CANDIDATE_TIPCOB,
    outer_query.c10estcob       AS CANDIDATE_ESTCOB,
    outer_query.account_nbr     AS ACCOUNT_NBR,
    outer_query.identificacion  AS CLIENT_ID_FROM_CANDIDATE,
    outer_query.currency_code   AS CURRENCY_CODE,
    outer_query.C10vacoor      AS CANDIDATE_VALCOOR,
    outer_query.c10vacope       AS CANDIDATE_VACOPE,
    outer_query.c10valcob       AS CANDIDATE_VALCOB,
    outer_query.c10descri       AS CANDIDATE_DESCRI,
    outer_query.c10fecori       AS CANDIDATE_ORIGINAL_DATE,
    outer_query.c10feveco       AS CANDIDATE_EXPIRATION_DATE
FROM (
         SELECT
             cob.c10numprod, cob.c10tipcob, cob.c10estcob, cob.c10vacope, cob.c10valcob, cob.c10descri,
             cob.C10vacoor,
             cob.c10fecori,
             cob.c10feveco,
             c11.c11nucuco,
             tap.account_nbr,
             tap.currency_code,
             iden.identificacion,
             ROW_NUMBER() OVER (PARTITION BY tap.account_nbr ORDER BY cob.c10feveco ASC, cob.c10numprod ASC) as rn
         FROM bdrdatos.cob010pf cob
                  INNER JOIN bdrdatos.cob011pf c11 ON cob.c10numprod = c11.c11numprod
                  INNER JOIN BDRQCD.TAP00201 tap ON c11.c11nucuco = tap.account_nbr
                  LEFT JOIN (
             SELECT cp9.CUX1AC, MIN(cp3.CUNTID) AS identificacion
             FROM BDRQCD.CUP00901 cp9
                      INNER JOIN BDRQCD.CUP00301 cp3 ON cp3.CUNBR = cp9.CUX1CS
             WHERE cp3.CUNTID <> '' GROUP BY cp9.CUX1AC
         ) iden ON iden.CUX1AC IN ((10000000000 + tap.account_nbr), (60000000000 + tap.account_nbr))
         WHERE tap.dmtyp = 1
           AND tap.dmstat = 1
           AND tap.dmcbal <= 0
           AND tap.currency_code = 0
           AND cob.c10valcob <= 0
           AND cob.c10vacoor > 0.05
           AND cob.c10tipcob = ?
           AND cob.c10estcob = ?
     ) outer_query
WHERE outer_query.rn = 1
ORDER BY RAND()
    FETCH FIRST ? ROWS ONLY

#QryCountTotalChargesInAccount
SELECT
    COUNT(*) AS TOTAL_CHARGE_COUNT
FROM bdrdatos.cob010pf cob
         INNER JOIN bdrdatos.cob011pf c11 ON cob.c10numprod = c11.c11numprod
WHERE c11.c11nucuco = ?

#QryGetCollectionById
SELECT
    cob.*,
    tap.account_nbr,
    tap.currency_code,
    iden.identificacion
FROM bdrdatos.cob010pf cob
         INNER JOIN bdrdatos.cob011pf c11 ON cob.c10numprod = c11.c11numprod
         INNER JOIN BDRQCD.TAP00201 tap ON c11.c11nucuco = tap.account_nbr
         LEFT JOIN (
    SELECT
        cp9.CUX1AC,
        MIN(cp3.CUNTID) AS identificacion
    FROM BDRQCD.CUP00901 cp9
             INNER JOIN BDRQCD.CUP00301 cp3 ON cp3.CUNBR = cp9.CUX1CS
    WHERE cp3.CUNTID <> ''
    GROUP BY cp9.CUX1AC
) iden ON iden.CUX1AC IN ((10000000000 + tap.account_nbr), (60000000000 + tap.account_nbr))
WHERE cob.c10numprod = ?
FETCH FIRST ? ROWS ONLY


#QryAccountTransactionEntries
SELECT
    T.DHBANK AS Banco_Transaccion,
    T.DHACCT AS Numero_Cuenta,
    T.DHDATE AS Fecha_Transaccion_J,
    TO_CHAR(DATE(LEFT(DHDATE, 4) || '-01-01') + (INTEGER(RIGHT(DHDATE, 3)) - 1) DAY, 'YYYY-MM-DD') AS Fecha_Transaccion_YYYYMMDD,
    T.DHAMT AS Monto_Transaccion,
    T.DHOTC AS Codigo_Transaccion,
    CUP.CUNBR AS Numero_Cliente,
    CUP.CUNTID AS IDCliente,
    CUP.CUNA1 AS Nombre_Completo,
    CUP.CUSHRT AS Nombre_Corto_Cliente,
    CTA.DMSHRT AS Nombre_Corto_Cuenta,
    CTA.DMTYP AS Tipo_Cuenta
FROM BDRQCD.Tap00501 T
INNER JOIN
    BDRQCD.TAP00201 CTA ON T.DHACCT = CTA.DMACCT
    AND T.DHBANK = CTA.DMBK
INNER JOIN
    BDRQCD.CUP00301 CUP ON CTA.DMSHRT = CUP.CUSHRT
WHERE
    T.ACCOUNT_NUMBER = ?
    AND TO_CHAR(DATE(LEFT(DHDATE, 4) || '-01-01') +
    (INTEGER(RIGHT(DHDATE, 3)) - 1) DAY, 'YYYYMMDD') = ?

#QryAccountTransactionDailyEntries
SELECT
    ACCOUNT_NUMBER,
    CAST(ACCOUNT_SHORT_NAME AS VARCHAR(255) CCSID 1208) AS ACCOUNT_SHORT_NAME,
    TRANSACTION_AMOUNT,
    PROCESSING_DATE,
    EFFECTIVE_DATE,
    DEBIT_CREDIT_CODE,
    TELLER_TRANSACTION_CODE,
    REVERSAL_CODE,
    TO_ACCOUNT_NUMBER,
    CAST(ALPHABETIC_DATA_1 AS VARCHAR(255) CCSID 1208) AS ALPHABETIC_DATA_1,
    CAST(ALPHABETIC_DATA_4 AS VARCHAR(255) CCSID 1208) AS ALPHABETIC_DATA_4,
    CAST(ALPHABETIC_DATA_7 AS VARCHAR(255) CCSID 1208) AS ALPHABETIC_DATA_7
FROM BDRQCP.PST00101
WHERE TLTPST = 'P'
  AND ACCOUNT_NUMBER = ?


#QryGetAccountBalanceAndTypeAccount
SELECT account_status, account_type, dmcbal
FROM BDRQCD.TAP00201 WHERE account_nbr = ?


#QryGetComssionPendingThadOnlyAccount
SELECT
    t.account_nbr,
    t.currency_code,
    c.c10vacoor,
    c.c10valcob,
    c.c10vacope
FROM bdrdatos.cob010pf c
INNER JOIN bdrdatos.cob011pf o ON c.c10numprod = o.c11numprod
INNER JOIN BDRQCD.TAP00201 t ON o.c11nucuco = account_nbr
WHERE c.c10vacope >= 0
AND o.c11nucuco = ?
FETCH FIRST 1 ROWS ONLY

#QryGetSavingAccountNumber
SELECT account_nbr, currency_code, account_status, account_type, dmcbal Balance
FROM BDRQCD.TAP00201 WHERE dmtyp =1 AND account_status = 1 ORDER BY RAND() FETCH FIRST 1 ROWS ONLY

#QryGetCurrentAccountNumber
SELECT account_nbr, currency_code, account_status, account_type, dmcbal Balance
FROM BDRQCD.TAP00201 WHERE dmtyp =6 ORDER BY RAND() FETCH FIRST 1 ROWS ONLY

#QryGetComissionById
SELECT bdrdatos.cob010pf.c10estcob AS STATUS,
bdrdatos.cob010pf.c10descri AS Description,
bdrdatos.cob010pf.C10TIPCOB AS Type_ofCharge,
bdrdatos.cob010pf.C10VACOOR AS Charge_Amount,
BDRQCD.CUP00901.CUX1CS,BDRQCD.TAP00201.account_nbr AS
CUSTOMER_CIF_KEY FROM bdrdatos.cob010pf INNER JOIN
bdrdatos.cob011pf ON bdrdatos.cob010pf.c10numprod =
bdrdatos.cob011pf.c11numprod INNER JOIN BDRQCD.TAP00201 ON
bdrdatos.cob011pf.c11nucuco = BDRQCD.TAP00201.account_nbr INNER
JOIN BDRQCD.CUP00901 ON BDRQCD.TAP00201.account_nbr=
bdrdatos.cob011pf.c11nucuco INNER JOIN BDRQCD.CUP00301 ON
BDRQCD.CUP00901.CUX1CS = BDRQCD.CUP00301.CUNBR WHERE
BDRQCD.TAP00201.dmtyp = 1 AND BDRQCD.TAP00201.dmstat = 1 AND
BDRQCD.CUP00901.CUX1CS = ? AND
bdrdatos.cob010pf.c10tipcob = 3 FETCH FIRST 1 ROWS ONLY

#QryGetSavingUnbalancedAccount
SELECT account_nbr,
       currency_code,
       account_status,
       account_type,
       dmcbal Balance
FROM BDRQCD.TAP00201
WHERE dmtyp = 1
  AND (dmcbal = 0)
ORDER BY RAND()
FETCH FIRST 1 ROWS ONLY


#QryGetCurrentsUnbalancedAccount
SELECT account_nbr,
       currency_code,
       account_status,
       account_type,
       dmcbal Balance
FROM BDRQCD.TAP00201
WHERE dmtyp = 6
  AND (dmcbal = 0)
ORDER BY RAND()
FETCH FIRST 1 ROWS ONLY


#QRY_Identificacion_Account_Balance
SELECT
    cp3.CUNTID AS IDENTIFICACION,
    acct.ACCOUNT_NBR AS NUMERO_CUENTA,
    acct.ACCOUNT_TYPE AS TIPO_CUENTA,
    acct.CURRENT_BALANCE AS SALDO_ACTUAL,
    acct.CURRENT_BALANCE - acct.HOLD_AMOUNT AS DISPONIBLE_HOY
FROM BDRQCD.TAP00201 acct
         INNER JOIN BDRQCD.CUP00901 cp9 ON cp9.CUX1AC IN ((10000000000 + acct.ACCOUNT_NBR), (60000000000 + acct.ACCOUNT_NBR))
         INNER JOIN BDRQCD.CUP00301 cp3 ON cp3.CUNBR = cp9.CUX1CS
WHERE acct.CURRENT_BALANCE > 450000
  AND cp3.CUNTID <> ''
  AND cp9.CUXREL NOT IN ('APO', 'AUT')
  AND acct.ACCOUNT_NBR NOT IN ('0105000299','0102515107')
  AND acct.ACCOUNT_PRODUCT_TYPE IN (74,77,78,34,234)
  AND cp3.CUNTID IN (
    SELECT cp3_sub.CUNTID
    FROM BDRQCD.TAP00201 acct_sub
             INNER JOIN BDRQCD.CUP00901 cp9_sub ON cp9_sub.CUX1AC IN ((10000000000 + acct_sub.ACCOUNT_NBR), (60000000000 + acct_sub.ACCOUNT_NBR))
             INNER JOIN BDRQCD.CUP00301 cp3_sub ON cp3_sub.CUNBR = cp9_sub.CUX1CS
    WHERE cp3_sub.CUNTID <> ''
      AND cp9.CUXREL NOT IN ('APO', 'AUT')
      AND acct_sub.ACCOUNT_NBR NOT IN ('0105000299','0102515107')
      AND acct.ACCOUNT_PRODUCT_TYPE IN (74,77,78,34,234)
    GROUP BY cp3_sub.CUNTID
    HAVING COUNT(acct_sub.ACCOUNT_NBR) = SUM(CASE WHEN acct_sub.ACCOUNT_STATUS = 1 THEN 1 ELSE 0 END)
       AND COUNT(acct_sub.ACCOUNT_NBR) <= 11
)
ORDER BY RAND()
    FETCH FIRST ? ROWS ONLY

#QRY_Identificacion_Account_Balance_RNC
SELECT
    cp3.TAX_ID_NUMBER AS RNC,
    acct.ACCOUNT_NBR AS NUMERO_CUENTA,
    acct.ACCOUNT_TYPE,
    acct.CURRENT_BALANCE AS SALDO_ACTUAL,
    acct.CURRENT_BALANCE - acct.HOLD_AMOUNT AS DISPONIBLE_HOY,
    acct.DMCMCN AS CODIGO_MONEDA,
    cp3.TAX_ID_NUMBER,
    cp9.CUXREL AS RELACION
FROM BDRQCD.TAP00201 acct
         INNER JOIN BDRQCD.CUP00901 cp9  ON cp9.CUX1AC IN ((10000000000 + acct.ACCOUNT_NBR), (60000000000 + acct.ACCOUNT_NBR))
         INNER JOIN BDRQCD.CUP00301 cp3  ON cp3.CUNBR = cp9.CUX1CS
WHERE acct.ACCOUNT_STATUS = 1
  AND acct.CURRENT_BALANCE > 450000
  AND cp3.TAX_ID_NUMBER <> ''
  AND cp3.CUSTOMER_TYPE = 2
  AND cp9.CUXREL NOT IN ('APO', 'AUT')
  AND acct.ACCOUNT_NBR NOT IN ('0105000299','0102515107')
  AND acct.ACCOUNT_PRODUCT_TYPE IN (74,77,78,34,234)
  AND cp3.TAX_ID_NUMBER IN (
    SELECT cp3_sub.TAX_ID_NUMBER
    FROM BDRQCD.TAP00201 acct_sub
             INNER JOIN BDRQCD.CUP00901 cp9_sub ON cp9_sub.CUX1AC IN ((10000000000 + acct_sub.ACCOUNT_NBR), (60000000000 + acct_sub.ACCOUNT_NBR))
             INNER JOIN BDRQCD.CUP00301 cp3_sub ON cp3_sub.CUNBR = cp9_sub.CUX1CS
    WHERE acct_sub.ACCOUNT_STATUS = 1
      AND cp3_sub.TAX_ID_NUMBER <> ''
      AND cp3_sub.CUSTOMER_TYPE = 2
      AND cp9.CUXREL NOT IN ('APO', 'AUT')
      AND acct.ACCOUNT_NBR NOT IN ('0105000299','0102515107')
      AND acct.ACCOUNT_PRODUCT_TYPE IN (74,77,78,34,234)
    GROUP BY cp3_sub.TAX_ID_NUMBER
    HAVING COUNT(acct_sub.ACCOUNT_NBR) = SUM(CASE WHEN acct_sub.ACCOUNT_STATUS = 1 THEN 1 ELSE 0 END)
       AND COUNT(acct_sub.ACCOUNT_NBR) <= 11
)
ORDER BY RAND()
    FETCH FIRST ? ROWS ONLY

#QRY_Identificacion_Account_Balance_By_currecyCodeEUR
SELECT
    cp3.CUNTID AS IDENTIFICACION,
    acct.ACCOUNT_NBR AS NUMERO_CUENTA,
    acct.ACCOUNT_TYPE,
    acct.CURRENT_BALANCE AS SALDO_ACTUAL,
    acct.CURRENT_BALANCE - acct.HOLD_AMOUNT AS DISPONIBLE_HOY,
    acct.DMCMCN AS CODIGO_MONEDA,
    cp3.TAX_ID_NUMBER,
    cp9.CUXREL AS RELACION
FROM BDRQCD.TAP00201 acct
         INNER JOIN BDRQCD.CUP00901 cp9  ON cp9.CUX1AC IN ((10000000000 + acct.ACCOUNT_NBR), (60000000000 + acct.ACCOUNT_NBR))
         INNER JOIN BDRQCD.CUP00301 cp3  ON cp3.CUNBR = cp9.CUX1CS
WHERE acct.CURRENT_BALANCE > 1500
  AND cp9.CUXREL NOT IN ('APO', 'AUT')
  AND acct.ACCOUNT_NBR NOT IN ('0105000299','0102515107')
  AND cp3.CUNTID <> ''
  AND acct.DMCMCN = ?
  AND cp3.CUNTID IN (
    SELECT cp3_sub.CUNTID
    FROM BDRQCD.TAP00201 acct_sub
             INNER JOIN BDRQCD.CUP00901 cp9_sub ON cp9_sub.CUX1AC IN ((10000000000 + acct_sub.ACCOUNT_NBR), (60000000000 + acct_sub.ACCOUNT_NBR))
             INNER JOIN BDRQCD.CUP00301 cp3_sub ON cp3_sub.CUNBR = cp9_sub.CUX1CS
    WHERE acct_sub.ACCOUNT_STATUS = 1
      AND cp9.CUXREL NOT IN ('APO', 'AUT')
      AND acct.ACCOUNT_NBR NOT IN ('0105000299','0102515107')
      AND cp3_sub.CUNTID <> ''
    GROUP BY cp3_sub.CUNTID
    HAVING COUNT(acct_sub.ACCOUNT_NBR) = SUM(CASE WHEN acct_sub.ACCOUNT_STATUS = 1 THEN 1 ELSE 0 END)
       AND COUNT(acct_sub.ACCOUNT_NBR) <= 11
)
ORDER BY RAND()
    FETCH FIRST 1 ROWS ONLY

#QRY_Identificacion_Account_Balance_By_currecyCodeUSD
SELECT
    cp3.CUNTID AS IDENTIFICACION,
    acct.ACCOUNT_NBR AS NUMERO_CUENTA,
    acct.ACCOUNT_TYPE,
    acct.CURRENT_BALANCE AS SALDO_ACTUAL,
    acct.CURRENT_BALANCE - acct.HOLD_AMOUNT AS DISPONIBLE_HOY,
    acct.DMCMCN AS CODIGO_MONEDA,
    cp3.TAX_ID_NUMBER,
    cp9.CUXREL AS RELACION
FROM BDRQCD.TAP00201 acct
         INNER JOIN BDRQCD.CUP00901 cp9  ON cp9.CUX1AC IN ((10000000000 + acct.ACCOUNT_NBR), (60000000000 + acct.ACCOUNT_NBR))
         INNER JOIN BDRQCD.CUP00301 cp3  ON cp3.CUNBR = cp9.CUX1CS
WHERE acct.CURRENT_BALANCE > 1500
  AND cp9.CUXREL NOT IN ('APO', 'AUT')
  AND acct.ACCOUNT_NBR NOT IN ('0105000299','0102515107')
  AND cp3.CUNTID <> ''
  AND acct.DMCMCN = ?
  AND cp3.CUNTID IN (
    SELECT cp3_sub.CUNTID
    FROM BDRQCD.TAP00201 acct_sub
             INNER JOIN BDRQCD.CUP00901 cp9_sub ON cp9_sub.CUX1AC IN ((10000000000 + acct_sub.ACCOUNT_NBR), (60000000000 + acct_sub.ACCOUNT_NBR))
             INNER JOIN BDRQCD.CUP00301 cp3_sub ON cp3_sub.CUNBR = cp9_sub.CUX1CS
    WHERE acct_sub.ACCOUNT_STATUS = 1
      AND cp9.CUXREL NOT IN ('APO', 'AUT')
      AND acct.ACCOUNT_NBR NOT IN ('0105000299','0102515107')
      AND cp3_sub.CUNTID <> ''
    GROUP BY cp3_sub.CUNTID
    HAVING COUNT(acct_sub.ACCOUNT_NBR) = SUM(CASE WHEN acct_sub.ACCOUNT_STATUS = 1 THEN 1 ELSE 0 END)
       AND COUNT(acct_sub.ACCOUNT_NBR) <= 11
)
ORDER BY RAND()
    FETCH FIRST 1 ROWS ONLY

#QryAllUnbalancedAccountsss
SELECT
    cp3.CUNTID AS cedula,
    acct.account_nbr,
    acct.account_type,
    acct.currency_code,
    acct.account_status,
    CASE
        WHEN acct.dmtyp = 1 THEN 'Ahorro'
        WHEN acct.dmtyp = 6 THEN 'Corriente'
        ELSE 'Otro'
    END AS tipo_cuenta,
    acct.dmcbal AS balance
FROM BDRQCD.TAP00201 acct
JOIN BDRQCD.CUP00901 cp9
    ON (
        cp9.CUX1AC = CAST(10000000000 AS BIGINT) + CAST(acct.account_nbr AS BIGINT)
        OR cp9.CUX1AC = CAST(60000000000 AS BIGINT) + CAST(acct.account_nbr AS BIGINT)
    )
JOIN BDRQCD.CUP00301 cp3
    ON cp3.CUNBR = cp9.CUX1CS
WHERE acct.dmcbal = 0
  AND acct.dmtyp IN (1, 6)
  AND cp3.CUNTID <> ''
ORDER BY acct.dmtyp, acct.account_nbr
FETCH FIRST 10 ROWS ONLY

#QryRandomAccountsWithAmountGreaterThan100K
SELECT account_nbr
FROM BDRQCD.TAP00201
WHERE account_status = 1
AND currency_code = 0
AND dmcbal >= 150000
AND MOD(account_nbr + MICROSECOND(CURRENT_TIMESTAMP), 23) = 0
FETCH FIRST 30 ROWS ONLY

#Qry_affectiveDate
SELECT CURRENT_DATE__CALENDAR FROM BDRQCD.TAP00101B tb WHERE BANK_NUMBER = 1

#Qry_consultCertificate
SELECT ACCOUNT_NUMBER
FROM BDRQCD.CUP00901 cp9
INNER JOIN BDRQCD.CUP00301 cp3 ON cp3.CUNBR = cp9.CUX1CS
INNER JOIN BDRQCD.TMP003SQ ccd ON ccd.ACCOUNT_CD_NBR = cp9.ACCOUNT_NUMBER
WHERE cp9.APPLICATION_NUMBER IN (30)
  AND ccd.CURRENT_BALANCE <= 600000
  AND cp9.CUSTOMER_CIF_KEY IN (
    SELECT CIF_INST FROM BDRDATOS.BDRINSTITU
)
ORDER BY RAND()
FETCH FIRST 1 ROWS ONLY

#Qry_ConsulcertificateActive
SELECT ACCOUNT_NUMBER
FROM BDRQCD.CUP00901 cp9
INNER JOIN BDRQCD.TMP003SQ ccd ON ccd.ACCOUNT_CD_NBR = cp9.ACCOUNT_NUMBER
WHERE cp9.APPLICATION_NUMBER IN (30)
  AND ccd.CURRENT_BALANCE <= 600000
  AND ccd.ACCOUNT_STATUS = 1
ORDER BY RAND()
FETCH FIRST 1 ROWS ONLY

#Qry_ConsultarPrestamos
SELECT ACCOUNT_NUMBER FROM BDRQCD.CUP00901 WHERE APPLICATION_NUMBER IN (50) AND CUSTOMER_CIF_KEY NOT IN (SELECT CIF_INST FROM BDRDATOS.BDRINSTITU)
AND ACCOUNT_NUMBER
IN (SELECT LNNOTE FROM BDRQCD.LNP00301 WHERE CURRENT_PRINCIPAL_BALANCE  > 0 )
ORDER BY RAND()
FETCH FIRST 1 ROWS ONLY

#QryRandomInactiveAccountsWithAmountGreaterThan100K
SELECT account_nbr
FROM BDRQCD.TAP00201
WHERE account_status = 5
AND currency_code = 0
AND dmcbal >= 150000
AND MOD(account_nbr + MICROSECOND(CURRENT_TIMESTAMP), 23) = 0
FETCH FIRST 30 ROWS ONLY