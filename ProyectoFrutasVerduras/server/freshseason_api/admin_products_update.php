<?php
header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['success' => false, 'message' => 'Método no permitido']);
    exit;
}

mysqli_report(MYSQLI_REPORT_OFF);

try {
    @include __DIR__ . "/db.php";
    if (!isset($conn) || !($conn instanceof mysqli)) {
        throw new Exception('No se pudo conectar a la BD (revisa db.php)');
    }
    $conn->set_charset('utf8mb4');

$id = isset($_POST['id']) ? (int) $_POST['id'] : 0;
$nombre = isset($_POST['nombre']) ? trim((string) $_POST['nombre']) : '';
$tipo = isset($_POST['tipo']) ? trim((string) $_POST['tipo']) : '';
$temporada = isset($_POST['temporada']) ? trim((string) $_POST['temporada']) : '';
$mesInicio = isset($_POST['mes_inicio']) ? trim((string) $_POST['mes_inicio']) : '';
$mesFin = isset($_POST['mes_fin']) ? trim((string) $_POST['mes_fin']) : '';
$beneficio = isset($_POST['beneficio']) ? trim((string) $_POST['beneficio']) : '';

if ($id <= 0 || $nombre === '' || $tipo === '') {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Faltan campos requeridos (id, nombre, tipo)']);
    exit;
}

$tipoNorm = mb_strtolower(trim($tipo), 'UTF-8');
$colorHex = ($tipoNorm === 'verdura') ? '#4CAF50' : '#FFB74D';

// imagen_nombre: usa el nombre como clave (sin acentos, minúsculas, guiones bajos)
$imagenNombre = mb_strtolower(trim($nombre), 'UTF-8');
$imagenNombre = iconv('UTF-8', 'ASCII//TRANSLIT', $imagenNombre);
$imagenNombre = preg_replace('/[^a-z0-9]+/', '_', $imagenNombre);
$imagenNombre = trim($imagenNombre, '_');
if ($imagenNombre === '') {
    $imagenNombre = ($tipoNorm === 'verdura') ? 'verdura' : 'fruta';
}

$mi = ($mesInicio === '' ? null : (int) $mesInicio);
$mf = ($mesFin === '' ? null : (int) $mesFin);
if ($mi !== null && ($mi < 1 || $mi > 12)) $mi = null;
if ($mf !== null && ($mf < 1 || $mf > 12)) $mf = null;

$stmt = $conn->prepare(
    "UPDATE productos
     SET nombre = ?,
         tipo = ?,
         temporada = NULLIF(?, ''),
         mes_inicio = NULLIF(?, ''),
         mes_fin = NULLIF(?, ''),
         beneficio = NULLIF(?, ''),
         color_hex = ?,
         imagen_nombre = ?
     WHERE id = ?"
);
if (!$stmt) {
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => 'Error en el servidor']);
    exit;
}

$miStr = ($mi === null) ? '' : (string) $mi;
$mfStr = ($mf === null) ? '' : (string) $mf;
$stmt->bind_param("ssssssssi", $nombre, $tipo, $temporada, $miStr, $mfStr, $beneficio, $colorHex, $imagenNombre, $id);
if ($stmt->execute()) {
    echo json_encode(['success' => true, 'message' => 'Producto actualizado']);
    exit;
}

http_response_code(500);
echo json_encode(['success' => false, 'message' => 'Error en el servidor']);

} catch (mysqli_sql_exception $e) {
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => 'SQL: ' . $e->getMessage()]);
    exit;
} catch (Throwable $e) {
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => 'Servidor: ' . $e->getMessage()]);
    exit;
}

