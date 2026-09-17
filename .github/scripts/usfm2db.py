#!/usr/bin/env python3
"""Download the 66 Malagasy USFM files and build baiboly.db (Public Domain, 1865)."""
import re, sqlite3, urllib.request, pathlib

BASE = "https://raw.githubusercontent.com/DavidHaslam/Malagasy-Bible-1865/main/USFM/"
FILES = [
    "01_GEN","02_EXO","03_LEV","04_NUM","05_DEU","06_JOS","07_JDG","08_RUT",
    "09_1SA","10_2SA","11_1KI","12_2KI","13_1CH","14_2CH","15_EZR","16_NEH",
    "17_EST","18_JOB","19_PSA","20_PRO","21_ECC","22_SNG","23_ISA","24_JER",
    "25_LAM","26_EZK","27_DAN","28_HOS","29_JOL","30_AMO","31_OBA","32_JON",
    "33_MIC","34_NAM","35_HAB","36_ZEP","37_HAG","38_ZEC","39_MAL",
    "41_MAT","42_MRK","43_LUK","44_JHN","45_ACT","46_ROM","47_1CO","48_2CO",
    "49_GAL","50_EPH","51_PHP","52_COL","53_1TH","54_2TH","55_1TI","56_2TI",
    "57_TIT","58_PHM","59_HEB","60_JAS","61_1PE","62_2PE","63_1JN","64_2JN",
    "65_3JN","66_JUD","67_REV"
]

def clean(text: str) -> str:
    # strip USFM markers, keep readable text
    text = re.sub(r"\\[a-z]+\d*\s*", " ", text)
    text = re.sub(r"\s+", " ", text)
    return text.strip()

book_no = 0
rows = []
for name in FILES:
    parts = name.split("_")
    book_no = int(parts[0])
    url = BASE + name + "_mg1865.usfm"
    print("Fetching", url)
    with urllib.request.urlopen(url) as r:
        content = r.read().decode("utf-8", errors="replace")
    chapter = 0
    verse = 0
    buf = []
    for line in content.splitlines():
        m_ch = re.match(r"\\\\c\s+(\d+)", line)
        m_v = re.match(r"\\\\v\s+(\d+)", line)
        if m_ch: chapter = int(m_ch.group(1))
        if m_v:
            if buf and verse > 0:
                rows.append((book_no*1000000 + chapter*1000 + verse, book_no, chapter, verse, clean(" ".join(buf))))
            verse = int(m_v.group(1))
            buf = [line[m_v.end():]]
        elif verse > 0:
            buf.append(line)
    if buf and verse > 0 and chapter > 0:
        rows.append((book_no*1000000 + chapter*1000 + verse, book_no, chapter, verse, clean(" ".join(buf))))

out = pathlib.Path("app/src/main/assets/baiboly.db")
out.parent.mkdir(parents=True, exist_ok=True)
if out.exists(): out.unlink()
con = sqlite3.connect(out)
con.execute("CREATE TABLE verses (id INTEGER PRIMARY KEY, book INTEGER, chapter INTEGER, verse INTEGER, text TEXT)")
con.execute("CREATE INDEX idx_book_chapter ON verses(book, chapter)")
con.executemany("INSERT INTO verses VALUES (?,?,?,?,?)", rows)
con.commit()
print(f"Wrote {len(rows)} verses to {out}")
