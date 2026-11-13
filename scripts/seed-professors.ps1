# Registra profesores de prueba en MS1 a traves de APIM.
# Uso (PowerShell):
#   $env:APIM_KEY = "<clave de suscripcion>"   # no se guarda en el repo
#   .\scripts\seed-professors.ps1
# Para probar el login con Google, agregue o cambie un correo por uno institucional real.

param(
    [string]$BaseUrl = "https://apimanagementcristopherarayavega.azure-api.net/revisatec"
)

$ErrorActionPreference = "Stop"

if (-not $env:APIM_KEY) {
    $env:APIM_KEY = [Environment]::GetEnvironmentVariable("APIM_KEY", "User")
}
if (-not $env:APIM_KEY) {
    throw "Defina la variable de entorno APIM_KEY antes de ejecutar el script."
}

$headers = @{ "Ocp-Apim-Subscription-Key" = $env:APIM_KEY }

# courseIds reales de MS1: 1 Bases de Datos, 4 Lenguajes, 5 Diseno de Software, 6 Administracion de Proyectos
$professors = @(
    @{ name = "Laura Mendez Solano";  email = "laura.mendez@itcr.ac.cr";  courseIds = @(5, 6) },
    @{ name = "Carlos Jimenez Urena"; email = "carlos.jimenez@itcr.ac.cr"; courseIds = @(1, 4) }
)

foreach ($professor in $professors) {
    $body = $professor | ConvertTo-Json -Compress
    try {
        $created = Invoke-RestMethod -Method Post -Uri "$BaseUrl/professors" `
            -Headers $headers -ContentType "application/json; charset=utf-8" `
            -Body ([System.Text.Encoding]::UTF8.GetBytes($body))
        Write-Host "Profesor $($created.name) (id $($created.id)) -> cursos $($created.courseIds -join ', ')"
    } catch {
        $status = $_.Exception.Response.StatusCode.value__
        if ($status -eq 409) {
            Write-Host "$($professor.email) ya existe, se omite"
        } else {
            Write-Warning "$($professor.email) fallo con HTTP $status"
        }
    }
}
