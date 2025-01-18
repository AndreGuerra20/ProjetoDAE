export const formatDate = (timestamp:string) => {
  const date = new Date(timestamp)
  let month = date.getMonth() + 1

  // dd-mm-yyyy hh:mm:ss
  const dateString = `${date.getDate() < 10 ? '0' + date.getDate() : date.getDate()}/${month + 1 < 10 ? '0' + month : month}/${date.getFullYear()}`
  const timeString = `${date.getHours() < 10 ? '0' + date.getHours() : date.getHours()}:${date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()}:${date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()}`

  return `${dateString} ${timeString}`

}

export const getMeasureText = (tipo:string) => {
  //const tipo = formData.selectedTipo;
  if (tipo === 'Pressao') {
    return 'KPa';
  } else if (tipo === 'Aceleracao') {
    return 'm/s²';
  } else if (tipo === 'Temperatura') {
    return '°C';
  } else if (tipo === 'Humidade') {
    return '%';
  }
  return '';
}
