import express from "express";
import cors from "cors";
import Database from "better-sqlite3";

const app = express();
app.use(cors());
app.use(express.json());

const db = new Database("studyflow.sqlite");
db.pragma("journal_mode = WAL");
db.exec(`
CREATE TABLE IF NOT EXISTS materias(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nome TEXT NOT NULL,
  professor TEXT DEFAULT ''
);
CREATE TABLE IF NOT EXISTS avaliacoes(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  titulo TEXT NOT NULL,
  tipo TEXT NOT NULL,
  materiaId INTEGER NOT NULL,
  dataMillis INTEGER NOT NULL,
  peso REAL NOT NULL,
  concluida INTEGER NOT NULL DEFAULT 0
);`);

app.get("/api/health", (_req,res)=>res.json({ok:true, service:"StudyFlow API"}));

app.get("/api/materias", (_req,res)=>{
  res.json(db.prepare("SELECT * FROM materias ORDER BY nome").all());
});
app.post("/api/materias", (req,res)=>{
  const nome=String(req.body.nome ?? "").trim();
  const professor=String(req.body.professor ?? "").trim();
  if(!nome) return res.status(400).json({error:"nome é obrigatório"});
  const info=db.prepare("INSERT INTO materias(nome,professor) VALUES(?,?)").run(nome,professor);
  res.status(201).json({id:Number(info.lastInsertRowid),nome,professor});
});

app.get("/api/avaliacoes", (_req,res)=>{
  res.json(db.prepare("SELECT * FROM avaliacoes ORDER BY dataMillis").all());
});
app.post("/api/avaliacoes", (req,res)=>{
  const {titulo,tipo,materiaId,dataMillis,peso}=req.body;
  if(!titulo || !tipo || !materiaId || !dataMillis || peso === undefined)
    return res.status(400).json({error:"campos obrigatórios"});
  if(!["Prova", "Trabalho", "Seminário"].includes(tipo))
    return res.status(400).json({error:"tipo inválido"});
  if(typeof peso !== "number" || peso < 0 || peso > 5)
    return res.status(400).json({error:"peso deve estar entre 0 e 5"});
  const materia=db.prepare("SELECT id FROM materias WHERE id=?").get(materiaId);
  if(!materia) return res.status(400).json({error:"matéria não encontrada"});
  const info=db.prepare(
    "INSERT INTO avaliacoes(titulo,tipo,materiaId,dataMillis,peso) VALUES(?,?,?,?,?)"
  ).run(titulo,tipo,materiaId,dataMillis,peso);
  res.status(201).json({id:Number(info.lastInsertRowid),...req.body,concluida:false});
});
app.patch("/api/avaliacoes/:id/concluida",(req,res)=>{
  const id=Number(req.params.id);
  const atual=db.prepare("SELECT concluida FROM avaliacoes WHERE id=?").get(id) as {concluida:number}|undefined;
  if(!atual) return res.status(404).json({error:"não encontrada"});
  const valor=req.body.concluida !== undefined ? (req.body.concluida?1:0) : atual.concluida?0:1;
  db.prepare("UPDATE avaliacoes SET concluida=? WHERE id=?").run(valor,id);
  res.json({ok:true,concluida:Boolean(valor)});
});

app.delete("/api/avaliacoes/:id",(req,res)=>{
  const info=db.prepare("DELETE FROM avaliacoes WHERE id=?").run(Number(req.params.id));
  if(!info.changes) return res.status(404).json({error:"avaliação não encontrada"});
  res.status(204).send();
});

app.delete("/api/materias/:id",(req,res)=>{
  const id=Number(req.params.id);
  const transaction=db.transaction(()=>{
    db.prepare("DELETE FROM avaliacoes WHERE materiaId=?").run(id);
    return db.prepare("DELETE FROM materias WHERE id=?").run(id);
  });
  const info=transaction();
  if(!info.changes) return res.status(404).json({error:"matéria não encontrada"});
  res.status(204).send();
});

app.listen(3000,()=>console.log("StudyFlow API em http://localhost:3000"));
