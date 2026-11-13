# Carga integrantes de prueba en MS1 a traves de APIM.
# Uso (PowerShell):
#   $env:APIM_KEY = "<clave de suscripcion>"   # no se guarda en el repo
#   .\scripts\seed-members.ps1
# Para probar el login con Google, cambie los correos de prueba por los correos
# reales de los integrantes antes de ejecutar.

param(
    [string]$BaseUrl = "https://apimanagementcristopherarayavega.azure-api.net/revisatec"
)

$ErrorActionPreference = "Stop"

if (-not $env:APIM_KEY) {
    throw "Defina la variable de entorno APIM_KEY antes de ejecutar el script."
}

$headers = @{ "Ocp-Apim-Subscription-Key" = $env:APIM_KEY }

# groupId -> integrantes (ids reales de MS1)
$members = @{
    1  = @(
        @{ name = "Andrea Solis Mora";     email = "andrea.solis@estudiantec.cr";    githubUser = "andreasolis" },
        @{ name = "Diego Rojas Vargas";    email = "diego.rojas@estudiantec.cr";     githubUser = "diegorojasv" },
        @{ name = "Mariana Chaves Ulate";  email = "mariana.chaves@estudiantec.cr";  githubUser = "marianachaves" }
    )
    5  = @(
        @{ name = "Jose Pablo Quesada";    email = "jose.quesada@estudiantec.cr";    githubUser = "jpquesada" },
        @{ name = "Valeria Jimenez Soto";  email = "valeria.jimenez@estudiantec.cr"; githubUser = "valejimenez" },
        @{ name = "Kevin Alfaro Brenes";   email = "kevin.alfaro@estudiantec.cr";    githubUser = "kalfaro" }
    )
    4  = @(
        @{ name = "Daniela Castro Mena";   email = "daniela.castro@estudiantec.cr";  githubUser = "danicastro" },
        @{ name = "Luis Fernando Arias";   email = "luis.arias@estudiantec.cr";      githubUser = "lfarias" },
        @{ name = "Sofia Herrera Campos";  email = "sofia.herrera@estudiantec.cr";   githubUser = "sofiaherrera" }
    )
    6  = @(
        @{ name = "Esteban Mora Picado";   email = "esteban.mora@estudiantec.cr";    githubUser = "estebanmora" },
        @{ name = "Camila Vega Salas";     email = "camila.vega@estudiantec.cr";     githubUser = "camivega" },
        @{ name = "Andres Navarro Ruiz";   email = "andres.navarro@estudiantec.cr";  githubUser = "anavarro" }
    )
    7  = @(
        @{ name = "Natalia Araya Gomez";   email = "natalia.araya@estudiantec.cr";   githubUser = "nataraya" },
        @{ name = "Sebastian Leon Cruz";   email = "sebastian.leon@estudiantec.cr";  githubUser = "sebleon" },
        @{ name = "Paula Montero Rivas";   email = "paula.montero@estudiantec.cr";   githubUser = "paulamontero" }
    )
    8  = @(
        @{ name = "Gabriel Sanchez Ortiz"; email = "gabriel.sanchez@estudiantec.cr"; githubUser = "gabsanchez" },
        @{ name = "Fernanda Rios Calvo";   email = "fernanda.rios@estudiantec.cr";   githubUser = "ferrios" },
        @{ name = "Ricardo Umana Pineda";  email = "ricardo.umana@estudiantec.cr";   githubUser = "rumana" }
    )
    9  = @(
        @{ name = "Isabel Murillo Leiva";  email = "isabel.murillo@estudiantec.cr";  githubUser = "isamurillo" },
        @{ name = "Mauricio Badilla Sol";  email = "mauricio.badilla@estudiantec.cr"; githubUser = "mbadilla" },
        @{ name = "Laura Cordero Fallas";  email = "laura.cordero@estudiantec.cr";   githubUser = "lauracordero" }
    )
    10 = @(
        @{ name = "Tomas Villalobos Mata"; email = "tomas.villalobos@estudiantec.cr"; githubUser = "tvillalobos" },
        @{ name = "Elena Barrantes Rojas"; email = "elena.barrantes@estudiantec.cr"; githubUser = "elenabarrantes" },
        @{ name = "Hugo Segura Alvarado";  email = "hugo.segura@estudiantec.cr";     githubUser = "hugosegura" }
    )
}

foreach ($groupId in ($members.Keys | Sort-Object)) {
    foreach ($member in $members[$groupId]) {
        $body = $member | ConvertTo-Json -Compress
        try {
            $created = Invoke-RestMethod -Method Post -Uri "$BaseUrl/groups/$groupId/members" `
                -Headers $headers -ContentType "application/json; charset=utf-8" `
                -Body ([System.Text.Encoding]::UTF8.GetBytes($body))
            Write-Host "Grupo $groupId -> $($created.name) (id $($created.id))"
        } catch {
            $status = $_.Exception.Response.StatusCode.value__
            if ($status -eq 409) {
                Write-Host "Grupo $groupId -> $($member.email) ya existe, se omite"
            } else {
                Write-Warning "Grupo $groupId -> $($member.email) fallo con HTTP $status"
            }
        }
    }
}
